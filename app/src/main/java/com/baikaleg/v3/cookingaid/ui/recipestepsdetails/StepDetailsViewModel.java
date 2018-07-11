package com.baikaleg.v3.cookingaid.ui.recipestepsdetails;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.baikaleg.v3.cookingaid.data.model.Step;

public class StepDetailsViewModel extends BaseObservable {
    public ObservableBoolean isVideo = new ObservableBoolean();
    public ObservableBoolean isImage = new ObservableBoolean();

    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> shortDescription = new ObservableField<>();

    public void setStep(Step step) {
        if (TextUtils.isEmpty(step.getVideoURL())) {
            isVideo.set(false);
        } else {
            isVideo.set(true);
        }
        if (TextUtils.isEmpty(step.getThumbnailURL())) {
            isImage.set(false);
        } else {
            isImage.set(true);
        }

        description.set(step.getDescription());
        shortDescription.set(step.getShortDescription());
        notifyChange();
    }

}