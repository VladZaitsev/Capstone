package com.baikaleg.v3.cookingaid.ui.basket;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;

public class BasketActivityModel  extends AndroidViewModel implements Observable {

    //region observable
    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    public BasketActivityModel(@NonNull Application application) {
        super(application);
    }

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
