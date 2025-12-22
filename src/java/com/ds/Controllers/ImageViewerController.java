package com.ds.Controllers;

import com.ds.Entities.Image;
import com.ds.Managers.ImageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public class ImageViewerController {

    @FXML
    private ImageView imageView;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblNoImage;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;

    private List<Image> imageList;
    private int currentIndex = 0;
    private ImageManager imageManager;

    public ImageViewerController() {
        this.imageManager = new ImageManager();
    }

    public void loadImages(int realEstateId) {
        this.imageList = imageManager.getImagesByRealEstateId(realEstateId);

        if (imageList == null || imageList.isEmpty()) {
            showNoImage();
        } else {
            currentIndex = 0;
            showImage(currentIndex);
        }
        updateButtons();
    }

    private void showImage(int index) {
        if (index >= 0 && index < imageList.size()) {
            Image imgEntity = imageList.get(index);

            String fullPath = imageManager.getFullPath(imgEntity.getPath());

            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(fullPath);
                imageView.setImage(img);
                lblTitle.setText("Görsel " + (index + 1) + " / " + imageList.size());

                imageView.setVisible(true);
                lblNoImage.setVisible(false);
            } catch (Exception e) {
                System.err.println("Resim yüklenemedi: " + fullPath);
            }
        }
    }

    private void showNoImage() {
        imageView.setVisible(false);
        lblNoImage.setVisible(true);
        lblTitle.setText("Görsel Bulunamadı");
        btnPrev.setDisable(true);
        btnNext.setDisable(true);
    }

    @FXML
    private void handleNext() {
        if (currentIndex < imageList.size() - 1) {
            currentIndex++;
            showImage(currentIndex);
            updateButtons();
        }
    }

    @FXML
    private void handlePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            showImage(currentIndex);
            updateButtons();
        }
    }

    private void updateButtons() {
        if (imageList == null || imageList.isEmpty()) return;

        btnPrev.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == imageList.size() - 1);
    }
}