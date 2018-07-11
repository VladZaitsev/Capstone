package com.baikaleg.v3.cookingaid.ui.recipestepsdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.baikaleg.v3.cookingaid.data.model.Step;

public class StepDetailsViewModel extends BaseObservable {

    @Bindable
    public MutableLiveData<String> description = new MutableLiveData<>();
    @Bindable
    public MutableLiveData<String> shortDescription = new MutableLiveData<>();

    public void setStep(Step step) {
        description.setValue(step.getDescription());
        shortDescription.setValue(step.getShortDescription());
        notifyChange();
    }

}