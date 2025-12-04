//package com.ds.Controllers;
//
//import com.ds.Entities.RealEstate;
//import com.ds.Managers.BuildingPropertyManager;
//import com.ds.Managers.PlotManager;
//import com.ds.Managers.RealEstateManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RealEstateController {
//
//    private final RealEstateManager realEstateManager;
//    private final PlotManager plotManager;
//    private final BuildingPropertyManager buildingPropertyManager;
//
//    public RealEstateController()
//    {
//        realEstateManager = new RealEstateManager();
//        plotManager = new PlotManager();
//        buildingPropertyManager = new BuildingPropertyManager();
//    }
//
//    public void GetAll()
//    {
//        List<RealEstate> realEstates = new ArrayList<>();
//        realEstates.addAll(plotManager.getAll());
//        realEstates.addAll(buildingPropertyManager.getAll());
//
//        for(RealEstate realEstate : realEstates)
//        {
//            System.out.println(realEstate.getTitle());
//        }
//    }
//
//    public void Create()
//    {
//
//    }
//
//
//}
