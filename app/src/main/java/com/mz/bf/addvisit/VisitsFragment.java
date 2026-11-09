package com.mz.bf.addvisit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mz.bf.api.MySharedPreference;
import com.mz.bf.databinding.FragmentVisitsBinding;

/** Fragment version of {@link VisitActivity}, shown from the bottom navigation. */
public class VisitsFragment extends Fragment implements VisitViewModel.VisitsView {
    private FragmentVisitsBinding binding;
    private VisitViewModel visitViewModel;
    private LinearLayoutManager layoutManager;
    private String user_id;
    private boolean isloading;
    private int previous_total = 0;
    private int page = 1;
    private static final int VIEW_THRESHOLD = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVisitsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_id = MySharedPreference.getInstance().Get_UserData(requireContext()).getId();
        layoutManager = new LinearLayoutManager(requireContext());
        binding.visitsRecycler.setLayoutManager(layoutManager);
        visitViewModel = new VisitViewModel(requireContext(), this);
        binding.visitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) return;
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int pastVisible = layoutManager.findFirstVisibleItemPosition();
                if (isloading && total > previous_total) {
                    isloading = false;
                    previous_total = total;
                }
                if (!isloading && (total - visible) <= pastVisible + VIEW_THRESHOLD) {
                    page++;
                    visitViewModel.PerformPagination(user_id, page);
                    isloading = true;
                }
            }
        });
        // reload each time the tab is opened so a newly added visit shows up
        page = 1;
        previous_total = 0;
        isloading = false;
        visitViewModel.get_all_visits(user_id, 1);
    }

    @Override
    public void init_sand(VisitAdapter visitAdapter) {
        if (binding == null) return;
        if (binding.visitsRecycler.getAdapter() != visitAdapter) {
            binding.visitsRecycler.setAdapter(visitAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
