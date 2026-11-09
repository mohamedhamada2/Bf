package com.mz.bf.addvisit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.databinding.FragmentAddVisitBinding;

import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Fragment version of {@link AddVisitActivity}, shown from the bottom navigation. */
public class AddVisitFragment extends Fragment implements OnLocationUpdatedListener,
        ClientAdapter.OnClientSelectedListener, AddVisitViewModel.OnVisitAddedListener {

    /** Implemented by the host activity to jump to the visits list after a visit is saved. */
    public interface Host {
        void showVisits();
    }

    private FragmentAddVisitBinding binding;
    private AddVisitViewModel addVisitViewModel;
    private ClientAdapter clientAdapter;
    private LinearLayoutManager clientsLayoutManager;
    private AlertDialog clientsDialog;
    private LocationTrack locationTrack;
    private String user_id, client_id;
    private Double latitude, longitude;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onPermissionsResult);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddVisitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_id = MySharedPreference.getInstance().Get_UserData(requireContext()).getId();
        addVisitViewModel = new AddVisitViewModel(requireContext(), this);
        binding.etClientName.setOnClickListener(v -> openClientsPopup());
        binding.btnAddVisit.setOnClickListener(v -> validation());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasLocationPermission()) {
            startLocationUpdates();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION});
        }
    }

    @Override
    public void onPause() {
        SmartLocation.with(requireContext()).location().stop();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationTrack != null) locationTrack.stopListener();
        if (clientsDialog != null) clientsDialog.dismiss();
        binding = null;
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void onPermissionsResult(Map<String, Boolean> result) {
        if (hasLocationPermission()) {
            startLocationUpdates();
        } else {
            Toast.makeText(requireContext(), "يجب السماح بتحديد الموقع لإضافة زيارة", Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates() {
        if (locationTrack == null) locationTrack = new LocationTrack(requireActivity());
        if (locationTrack.canGetLocation()) {
            LocationParams params = new LocationParams.Builder()
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setDistance(1f)
                    .setInterval(5 * 1000)
                    .build();
            new SmartLocation.Builder(requireContext()).logging(true).build()
                    .location().config(params).start(this);
        } else {
            locationTrack.showSettingsAlert();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private void validation() {
        if (TextUtils.isEmpty(client_id)) {
            Toast.makeText(requireContext(), "اختر العميل أولاً", Toast.LENGTH_SHORT).show();
            return;
        }
        if (latitude == null || longitude == null) {
            Toast.makeText(requireContext(), "جاري تحديد الموقع، حاول مرة أخرى", Toast.LENGTH_SHORT).show();
            return;
        }
        String notes = binding.etNotes.getText().toString();
        addVisitViewModel.add_visit(client_id, latitude, longitude, notes, user_id);
    }

    @Override
    public void onVisitAdded() {
        client_id = null;
        if (binding != null) {
            binding.etClientName.setText("");
            binding.etNotes.setText("");
        }
        if (getActivity() instanceof Host) {
            ((Host) getActivity()).showVisits();
        }
    }

    @Override
    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        if (binding != null) binding.etClientName.setText(client.getClientName());
        if (clientsDialog != null) clientsDialog.dismiss();
    }

    // ---- clients picker (same behaviour as AddVisitActivity) ----

    private void openClientsPopup() {
        final int[] page = {1};
        final boolean[] isloading = {false};
        final int[] previous_total = {0};
        final int view_threshold = 20;
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.products_dialog, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        clientsLayoutManager = new LinearLayoutManager(requireContext());
        clients_recycler.setLayoutManager(clientsLayoutManager);
        clients_recycler.setHasFixedSize(true);
        loadClients(clients_recycler, null);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                page[0] = 1;
                previous_total[0] = 0;
                isloading[0] = false;
                loadClients(clients_recycler, s.toString());
            }
        });
        clients_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0 || !TextUtils.isEmpty(et_search.getText())) return;
                int visible = clientsLayoutManager.getChildCount();
                int total = clientsLayoutManager.getItemCount();
                int pastVisible = clientsLayoutManager.findFirstVisibleItemPosition();
                if (isloading[0] && total > previous_total[0]) {
                    isloading[0] = false;
                    previous_total[0] = total;
                }
                if (!isloading[0] && (total - visible) <= pastVisible + view_threshold) {
                    page[0]++;
                    loadMoreClients(page[0]);
                    isloading[0] = true;
                }
            }
        });

        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        clientsDialog = new AlertDialog.Builder(requireContext()).setView(view).create();
        clientsDialog.show();
        Window window = clientsDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        cancel_img.setOnClickListener(v -> clientsDialog.dismiss());
    }

    private void loadClients(RecyclerView recycler, @Nullable String query) {
        Context context = getContext();
        if (context == null || !Utilities.isNetworkAvailable(context)) return;
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ClientModel> call = TextUtils.isEmpty(query)
                ? service.get_clients(user_id, 1)
                : service.search_clients(user_id, query, 1);
        call.enqueue(new Callback<ClientModel>() {
            @Override
            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                if (response.isSuccessful() && response.body() != null && getContext() != null) {
                    clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), AddVisitFragment.this);
                    recycler.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onFailure(Call<ClientModel> call, Throwable t) { }
        });
    }

    private void loadMoreClients(int page) {
        Context context = getContext();
        if (context == null || !Utilities.isNetworkAvailable(context)) return;
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        service.get_clients(user_id, page).enqueue(new Callback<ClientModel>() {
            @Override
            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                if (response.isSuccessful() && response.body() != null
                        && clientAdapter != null && !response.body().getClients().isEmpty()) {
                    clientAdapter.add_client(response.body().getClients());
                }
            }

            @Override
            public void onFailure(Call<ClientModel> call, Throwable t) { }
        });
    }
}
