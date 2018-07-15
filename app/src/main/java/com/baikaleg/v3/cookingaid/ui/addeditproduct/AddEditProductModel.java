package com.baikaleg.v3.cookingaid.ui.addeditproduct;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.baikaleg.v3.cookingaid.data.Repository;

public class AddEditProductModel extends ViewModel implements Observable {
    private static final String TAG = AddEditProductModel.class.getSimpleName();
    private final Repository repository;
    private AddEditProductEventNavigator navigator;

    public AddEditProductModel(Repository repository) {
        this.repository = repository;
        //this.navigator=navigator;
    }

   /* @Bindable
    public MutableLiveData<Product> product = new MutableLiveData<>();

    public void saveProduct(Product newProduct) {

    }*/

    public void loadProduct(String uuid) {
        notifyChange();
    }

    //region observable
    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    private void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    private void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }
    //endregion
}
