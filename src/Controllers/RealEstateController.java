package Controllers;

import Entities.RealEstate;
import Managers.BuildingPropertyManager;
import Managers.PlotManager;
import Managers.RealEstateManager;

import java.util.ArrayList;
import java.util.List;

public class RealEstateController {

    private final RealEstateManager realEstateManager;
    private final PlotManager plotManager;
    private final BuildingPropertyManager buildingPropertyManager;
    public RealEstateController()
    {
        realEstateManager = new RealEstateManager();
        plotManager = new PlotManager();
        buildingPropertyManager = new BuildingPropertyManager();
    }

    public void GetAll()
    {
        List<RealEstate> realEstates = new ArrayList<>();
        realEstates.addAll(plotManager.getAll());
        realEstates.addAll(buildingPropertyManager.getAll());

        for(RealEstate realEstate : realEstates)
        {
            System.out.println(realEstate.getTitle());
        }
    }

    public void Create()
    {

    }


}
