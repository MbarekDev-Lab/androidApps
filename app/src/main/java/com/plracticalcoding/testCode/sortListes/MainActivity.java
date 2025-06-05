package com.plracticalcoding.testCode.sortListes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
// import android.widget.Toast; // For simple feedback
import androidx.appcompat.app.AppCompatActivity;
// import androidx.recyclerview.widget.LinearLayoutManager;
// import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date; // Assuming you have a Date object for sorting
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Replace MyDataItem with your actual data class
    public static class MyDataItem {
        public int id;
        public String name;
        public Date date; // Or long for timestamp

        public MyDataItem(int id, String name, Date date) {
            this.id = id;
            this.name = name;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toString() { // For simple display in logs/Toast
            return "MyDataItem{" +
                    "name='" + name + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

    private Spinner sortSpinner;
    // private RecyclerView recyclerViewItems;
    // private MyListAdapter myAdapter; // Your RecyclerView adapter

    private List<MyDataItem> originalList;
    private List<MyDataItem> displayedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your layout file

        sortSpinner = findViewById(R.id.sortSpinner);
        // recyclerViewItems = findViewById(R.id.recyclerViewItems);

        // Initialize sample data
        originalList = new ArrayList<>();
        originalList.add(new MyDataItem(1, "Apple", new Date(System.currentTimeMillis() - 200000)));
        originalList.add(new MyDataItem(2, "Cherry", new Date(System.currentTimeMillis() - 100000)));
        originalList.add(new MyDataItem(3, "Banana", new Date(System.currentTimeMillis())));

        displayedList = new ArrayList<>(originalList);

        // Setup RecyclerView (if you have one)
        // myAdapter = new MyListAdapter(displayedList, this); // Your adapter
        // recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        // recyclerViewItems.setAdapter(myAdapter);

        setupSortSpinner();
    }

    private void setupSortSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                sortList(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void sortList(String criteria) {
        // It's good practice to sort a copy or re-sort the original
        // and then update the adapter's list.
        List<MyDataItem> listToSort = new ArrayList<>(originalList); // Sort a copy

        if (criteria.equals(getString(R.string.sort_name_az))) { // Example: getString for localization

            Collections.sort(listToSort, new Comparator<MyDataItem>() {
                @Override
                public int compare(MyDataItem o1, MyDataItem o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });

        } else if (criteria.equals(getString(R.string.sort_name_za))) {
            Collections.sort(listToSort, new Comparator<MyDataItem>() {
                @Override
                public int compare(MyDataItem o1, MyDataItem o2) {
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
        } else if (criteria.equals(getString(R.string.sort_date_newest))) {
            Collections.sort(listToSort, new Comparator<MyDataItem>() {
                @Override
                public int compare(MyDataItem o1, MyDataItem o2) {
                    return o2.getDate().compareTo(o1.getDate()); // Newest first
                }
            });
        } else if (criteria.equals(getString(R.string.sort_date_oldest))) {
            Collections.sort(listToSort, new Comparator<MyDataItem>() {
                @Override
                public int compare(MyDataItem o1, MyDataItem o2) {
                    return o1.getDate().compareTo(o2.getDate()); // Oldest first
                }
            });
        }
        // No explicit "else" needed if "Sort by..." doesn't change the order from originalList

        displayedList.clear();
        displayedList.addAll(listToSort);

        // Notify your RecyclerView adapter that the data has changed
        // if (myAdapter != null) {
        //     myAdapter.notifyDataSetChanged();
        // }

        // For demonstration purposes
        System.out.println("List sorted by: " + criteria);
        for (MyDataItem item : displayedList) {
            System.out.println(item.toString());
        }
        // Toast.makeText(this, "Sorted by: " + criteria, Toast.LENGTH_SHORT).show();
    }

    // Helper method to make string comparisons cleaner, especially with localization
    // (You'd add these to your strings.xml if you use them)
    private String getString(int resId) {
        return getResources().getString(resId);
    }
}

// You would also need a strings.xml like this:
/*
<resources>

</resources>
*/