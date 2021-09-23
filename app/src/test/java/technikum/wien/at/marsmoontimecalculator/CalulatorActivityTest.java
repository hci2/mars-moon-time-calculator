package technikum.wien.at.marsmoontimecalculator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalulatorActivityTest {

    private static CalculatorActivity calculatorActivity;

    @BeforeClass
    public static void init() {
        calculatorActivity = new CalculatorActivity();
    }

    @Test
    public void calculateMarsInterval_NestedDSetPRise_Ok() {
        assertEquals(100, calculatorActivity.manageCalculateMarsInterval(1391, 2305, 2205,2445));
    }

    @Test
    public void calculateMarsInterval_NestedDRisePSet_Ok() {
        assertEquals(807, calculatorActivity.manageCalculateMarsInterval(1400, 2240, 1020,2207));
    }

    @Test
    public void calculateMarsInterval_NestedNextDay_Ok() {
        assertEquals(600, calculatorActivity.manageCalculateMarsInterval(2200, 600, 2300,400));
    }

    @Test
    public void calculateMarsInterval_Overlap_Ok() {
        assertEquals(619, calculatorActivity.manageCalculateMarsInterval(1400, 2240, 1588,2207));
    }

    @Test
    public void calculateMarsInterval_NextdayFirstInterval_Ok() {
        assertEquals(200, calculatorActivity.manageCalculateMarsInterval(2453, 712, 512,845));
    }

    @Test
    public void calculateMarsInterval_NextdayFirstIntervalSecondZero_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(845,  500, 0 ,0 ));
    }

    @Test
    public void calculateMarsInterval_NextdaySecondIntervalFirstZero_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(0,  0, 2231 ,399 ));
    }

    @Test
    public void calculateMarsInterval_NextdaySecondIntervalMidnightFirstZero_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(0,  0, 2231 ,0 ));
    }

    @Test
    public void calculateMarsInterval_NextdaySecondInterval_Ok() {
        assertEquals(300, calculatorActivity.manageCalculateMarsInterval(512, 845, 2453 ,812 ));
    }

    @Test
    public void calculateMarsInterval_NextdayBothInterval_Ok() {
        assertEquals(1045, calculatorActivity.manageCalculateMarsInterval(1855, 497, 1039,400));
    }

    @Test
    public void calculateMarsInterval_TouchEndFirstStartSecondInterval_Ok() {
        assertEquals(347, calculatorActivity.manageCalculateMarsInterval(1232, 1706, 1706,1578));
    }

    @Test
    public void calculateMarsInterval_TouchStartFirstEndSecondInterval_Ok() {
        assertEquals(1, calculatorActivity.manageCalculateMarsInterval(2211, 36, 700,2211));
    }

    @Test
    public void calculateMarsInterval_TouchMidnight25_Ok() {
        assertEquals(1, calculatorActivity.manageCalculateMarsInterval(1000, 2500, 2500,500));
    }

    @Test
    public void calculateMarsInterval_TouchMidnightZero_Ok() {
        assertEquals(1, calculatorActivity.manageCalculateMarsInterval(1000, 0, 0,500));
    }

    @Test
    public void calculateMarsInterval_NestedBothSameIntervals_Ok() {
        assertEquals(200, calculatorActivity.manageCalculateMarsInterval(1000, 1200, 1000,1200));
    }

    @Test
    public void calculateMarsInterval_BothIntervalsZero_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(0, 0, 0,0));
    }

    @Test
    public void calculateMarsInterval_BothIntervals25_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(2500, 2500, 2500,2500));
    }

    @Test
    public void calculateMarsInterval_BothIntervalsSame_Ok() {
        assertEquals(0, calculatorActivity.manageCalculateMarsInterval(15, 15, 15,15));
    }
}
