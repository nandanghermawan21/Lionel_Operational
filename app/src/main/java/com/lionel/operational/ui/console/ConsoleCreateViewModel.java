package com.lionel.operational.ui.console;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCreateViewModel extends ViewModel {
    private List<String> sttItems;
    private List<String> destinationItems;

    public List<String> getSttItems() {
        if (sttItems == null) {
            initDataSttList();
        }
        return sttItems;
    }

    private void initDataSttList() {
        // Isi dataList dengan data yang diinginkan
        sttItems = new ArrayList<>();
        sttItems.add("Item 1");
        sttItems.add("Item 2");
        sttItems.add("Item 3");
        // Tambahkan data lainnya sesuai kebutuhan
    }

    public List<String> getDestinationItems() {
        if (destinationItems == null) {
            initDataSttList();
        }
        return destinationItems;
    }

    private void initDataDepartmentList() {
        // Isi dataList dengan data yang diinginkan
        destinationItems = new ArrayList<>();
        destinationItems.add("Item 1");
        destinationItems.add("Item 2");
        destinationItems.add("Item 3");
        // Tambahkan data lainnya sesuai kebutuhan
    }
}