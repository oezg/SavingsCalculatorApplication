package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SavingsCalculatorApplication extends Application {

    public static void main(String[] args) {
        launch(SavingsCalculatorApplication.class);
    }
    
    @Override
    public void start(Stage stage) {
        VBox top = new VBox();
        
        Label leftMonthly = new Label("Monthly savings");
        Slider sliderMonthly = new Slider(25, 250, 50);
        sliderMonthly.setMajorTickUnit(25);
        sliderMonthly.setMinorTickCount(5);
        sliderMonthly.setSnapToTicks(true);
        sliderMonthly.setShowTickMarks(true);
        sliderMonthly.setShowTickLabels(true);
        Label rightMonthly = new Label();
        rightMonthly.setText(String.valueOf(sliderMonthly.getValue()));
        
        BorderPane monthlySavings = new BorderPane();
        monthlySavings.setPadding(new Insets(20));
        monthlySavings.setLeft(leftMonthly);
        monthlySavings.setCenter(sliderMonthly);
        monthlySavings.setRight(rightMonthly);
        top.getChildren().add(monthlySavings);
        
        Label leftYearly = new Label("Yearly interest rate");
        Slider sliderYearly = new Slider(0, 10, 5);
        sliderYearly.setMajorTickUnit(1);
        sliderYearly.setMinorTickCount(5);
        sliderYearly.setSnapToTicks(true);
        sliderYearly.setShowTickMarks(true);
        sliderYearly.setShowTickLabels(true);
        Label rightYearly = new Label();
        rightYearly.setText(String.valueOf(sliderYearly.getValue()));
        
        BorderPane yearlyInterest = new BorderPane();
        yearlyInterest.setPadding(new Insets(20));
        yearlyInterest.setLeft(leftYearly);
        yearlyInterest.setCenter(sliderYearly);
        yearlyInterest.setRight(rightYearly);
        top.getChildren().add(yearlyInterest);
                
        XYChart.Series without = new XYChart.Series();
        without.setName("Without Interest");
        XYChart.Series compound = new XYChart.Series<>();
        compound.setName("Compound Interest");
        updateChart(sliderMonthly.getValue(), sliderYearly.getValue(), without, compound);
        
        NumberAxis xAxis = new NumberAxis(0, 30, 1);
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Savings account");
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);        
        
        sliderMonthly.setOnMouseClicked((event) -> {
            rightMonthly.setText(String.format("%.2f", sliderMonthly.getValue()));
            updateChart(sliderMonthly.getValue(), sliderYearly.getValue(), without, compound);
        });
        
        sliderYearly.setOnMouseClicked((event) -> {
            rightYearly.setText(String.format("%.2f", sliderYearly.getValue()));
            updateChart(sliderMonthly.getValue(), sliderYearly.getValue(), without, compound);
        });
        
        lineChart.getData().add(without);
        lineChart.getData().add(compound);
        
        BorderPane layout = new BorderPane();
        layout.setTop(top);
        layout.setCenter(lineChart);
        
        Scene scene = new Scene(layout);
        
        stage.setScene(scene);
        stage.setTitle("Savings Calculator");
        stage.show();
    }
    
    private void updateChart(double money, double rate, XYChart.Series without, XYChart.Series compound) {
        without.getData().clear();
        compound.getData().clear();
        for (int i = 0; i < 31; i++) {
            without.getData().add(new XYChart.Data(i, i * money * 12));
            compound.getData().add(new XYChart.Data(i, cumulate(money, rate, i)));
        }
    }
    
    private double cumulate(double money, double rate, int year) {
        rate /= 100;
        return money * 12 * (Math.pow(1 + rate, year) - 1) / rate * (1 + rate);
    }
    
    

}
