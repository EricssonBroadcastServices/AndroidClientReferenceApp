package com.emp.demo.interfaces;

import net.ericsson.emovs.utilities.models.EmpAsset;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 23/07/2017.
 */

public interface IAssetCarouselAdapter {
    public void setAssets(ArrayList<EmpAsset> assets);
    public void notifyDataSetChanged();
}