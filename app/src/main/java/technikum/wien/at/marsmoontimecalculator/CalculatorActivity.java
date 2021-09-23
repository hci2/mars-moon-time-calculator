package technikum.wien.at.marsmoontimecalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.Logger;

import static technikum.wien.at.marsmoontimecalculator.Consts.END_OF_DAY;
import static technikum.wien.at.marsmoontimecalculator.Consts.RESULT_LENGTH;
import static technikum.wien.at.marsmoontimecalculator.Consts.RESULT_OKAY;
import static technikum.wien.at.marsmoontimecalculator.Consts.RESULT_OVER_UPPER_LIMIT;
import static technikum.wien.at.marsmoontimecalculator.Consts.RESULT_START_END_SAME;
import static technikum.wien.at.marsmoontimecalculator.Consts.RESULT_WRONG_FORMAT;
import static technikum.wien.at.marsmoontimecalculator.Consts.START_OF_DAY;
import static technikum.wien.at.marsmoontimecalculator.Consts.TAG_CALCULATORACTIVITY;

public class CalculatorActivity extends AppCompatActivity {

    private long deimosRiseTimestamp;
    private long deimosSetTimestamp;
    private long phobosRiseTimestamp;
    private long phobosSetTimestamp;

    private EditText mDeimosRiseTimestamp;
    private EditText mDeimosSetTimestamp;
    private EditText mPhobosRiseTimestamp;
    private EditText mPhobosSetTimestamp;
    private TextView mCalculationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mDeimosRiseTimestamp = (EditText) findViewById(R.id.deimos_rise_ts);
        mDeimosSetTimestamp = (EditText) findViewById(R.id.deimos_set_ts);
        mPhobosRiseTimestamp = (EditText) findViewById(R.id.phobos_rise_ts);
        mPhobosSetTimestamp = (EditText) findViewById(R.id.phobos_set_ts);
        mCalculationResult = (TextView) findViewById(R.id.calculationResult);
    }

    public void submitCalculation(View view){
        String result=checkInputsReturnCodeAndParse();
        if(result.equals(RESULT_OKAY)){
//            Interval interval1 = new Interval(deimosRiseTimestamp, deimosSetTimestamp);
//            Interval interval2 = new Interval(phobosRiseTimestamp, phobosSetTimestamp);
            long a1=deimosRiseTimestamp;
            long e1=deimosSetTimestamp;
            long a2=phobosRiseTimestamp;
            long e2=phobosSetTimestamp;
            long marsInterval = manageCalculateMarsInterval(a1, e1, a2, e2);

            mCalculationResult.setText("Result: Mars-interval is " + marsInterval + " minutes");
        } else {
            mCalculationResult.setText("Error: "+result);
        }

    }

    public long manageCalculateMarsInterval(long a1, long e1, long a2, long e2) {
        long marsInterval = 0;
        if(checkStartAndEndIntervalSame(a1, e1)){
            return 0;
        }
        if(checkStartAndEndIntervalSame(a2, e2)){
            return 0;
        }
        if(isNextDay(a1, e1) && isNextDay(a2, e2)){ //interval 1 and 2 goes over midnight
            long a1First = a1;
            long e1First = END_OF_DAY;
            long a1Second = START_OF_DAY;
            long e1Second = e1;
            long a2First = a2;
            long e2First = END_OF_DAY;
            long a2Second = START_OF_DAY;
            long e2Second = e2;

            marsInterval += calculateMarsInterval(a1First, e1First, a2First, e2First);
            marsInterval += calculateMarsInterval(a1First, e1First, a2Second, e2Second);
            marsInterval += calculateMarsInterval(a1Second, e1Second, a2First, e2First);
            marsInterval += calculateMarsInterval(a1Second, e1Second, a2Second, e2Second);
        } else if(isNextDay(a1, e1)){ //interval 1 goes over midnight
            long a1First = a1;
            long e1First = END_OF_DAY;
            long a1Second = START_OF_DAY;
            long e1Second = e1;

            marsInterval += calculateMarsInterval(a1First, e1First, a2, e2);
            marsInterval += calculateMarsInterval(a1Second, e1Second, a2, e2);
        } else if(isNextDay(a2, e2)){ //interval 2 goes over midnight
            long a2First = a2;
            long e2First = END_OF_DAY;
            long a2Second = START_OF_DAY;
            long e2Second = e2;

            marsInterval += calculateMarsInterval(a2First, e2First, a1, e1);
            marsInterval += calculateMarsInterval(a2Second, e2Second, a1, e1);
        } else { // no NEXTDAY
            marsInterval = calculateMarsInterval(a1, e1, a2, e2);
        }
        return marsInterval;
    }

    private long calculateMarsInterval(long a1, long e1, long a2, long e2){
        if(checkAndCalculateNested(a1, e1, a2, e2) !=0){
            return checkAndCalculateNested(a1, e1, a2, e2);
        }

        if(checkAndCalculateOverlap(a1, e1, a2, e2) !=0){
            return checkAndCalculateOverlap(a1, e1, a2, e2);
        }

        if(checkAndCalculateTouch(a1, e1, a2, e2) !=0){
            return checkAndCalculateTouch(a1, e1, a2, e2);
        }
        return 0;
    }

    private long checkAndCalculateNested(long a1, long e1, long a2, long e2) {
        if(a1 <= a2 && e1 >= e2) { // interval 2 is within interval 1
            return e2 - a2;
        }

        if(a2 <=a1 && e2 >= e1) { // interval 1 is within interval 2
            return e1 - a1;
        }
        return 0;
    }

    private long checkAndCalculateOverlap(long a1, long e1, long a2, long e2) {
        if(e1 > a2 && a1 < e2) {
            return  Math.min(e1, e2) - Math.max(a1, a2);
        }
        return 0;
    }

    private long checkAndCalculateTouch(long a1, long e1, long a2, long e2) {
        if(e1 == a2 || e2 == a1 && a1 != a2 && e1 != e2) {
            return 1;
        }
        //Exception for END_OF_DAY touch
        if(e1 == END_OF_DAY && e1 == a2 || e1 == e2) {
            return 1;
        }
        //Exception for END_OF_DAY touch
        if(a1 == END_OF_DAY && a1 == a2 || a1 == e2) {
            return 1;
        }

        return 0;
    }

    private String checkInputsReturnCodeAndParse() {
        mDeimosRiseTimestamp.setText(mDeimosRiseTimestamp.getText().toString().trim());
        mDeimosSetTimestamp.setText(mDeimosSetTimestamp.getText().toString().trim());
        mPhobosRiseTimestamp.setText(mPhobosRiseTimestamp.getText().toString().trim());
        mPhobosSetTimestamp.setText(mPhobosSetTimestamp.getText().toString().trim());
        checkEmpty(mDeimosRiseTimestamp);
        if (checkLength(mDeimosRiseTimestamp.getText().toString())) return RESULT_LENGTH;
        checkEmpty(mDeimosSetTimestamp);
        if (checkLength(mDeimosSetTimestamp.getText().toString())) return RESULT_LENGTH;

        checkEmpty(mPhobosRiseTimestamp);
        if (checkLength(mPhobosRiseTimestamp.getText().toString())) return RESULT_LENGTH;
        checkEmpty(mPhobosSetTimestamp);
        if (checkLength(mPhobosSetTimestamp.getText().toString())) return RESULT_LENGTH;

        try {
            deimosRiseTimestamp = parseTimeStringToLong(mDeimosRiseTimestamp.getText().toString());
            if(checkOverUpperLimit(deimosRiseTimestamp)) return RESULT_OVER_UPPER_LIMIT;
            deimosSetTimestamp = parseTimeStringToLong(mDeimosSetTimestamp.getText().toString());
            if(checkOverUpperLimit(deimosSetTimestamp)) return RESULT_OVER_UPPER_LIMIT;
            phobosRiseTimestamp = parseTimeStringToLong(mPhobosRiseTimestamp.getText().toString());
            if(checkOverUpperLimit(phobosRiseTimestamp)) return RESULT_OVER_UPPER_LIMIT;
            phobosSetTimestamp = parseTimeStringToLong(mPhobosSetTimestamp.getText().toString());
            if(checkOverUpperLimit(phobosSetTimestamp)) return RESULT_OVER_UPPER_LIMIT;
            if(checkStartAndEndIntervalSame(deimosRiseTimestamp, deimosSetTimestamp)) return RESULT_START_END_SAME;
            if(checkStartAndEndIntervalSame(phobosRiseTimestamp, phobosSetTimestamp)) return RESULT_START_END_SAME;
        } catch (Exception e){
            Log.i(TAG_CALCULATORACTIVITY, "Input is wrong, exception: "+e.getMessage());
            return RESULT_WRONG_FORMAT;
        }
        return RESULT_OKAY;
    }

    private boolean checkStartAndEndIntervalSame(long start, long end) {
        if(start == end){
            return true;
        }
        return false;
    }


    private boolean checkOverUpperLimit(long number) {
        if(number>END_OF_DAY){
            return true;
        }
        return false;
    }

    private boolean checkLength(String text) {
        if (text.length() > 5) {
            return true;
        }
        return false;
    }

    private void checkEmpty(EditText editText) {
        if(editText.getText().toString().equals("")){
            editText.setText("00:00");
        }
    }

    private long parseTimeStringToLong(String timestamp) {
        return Long.parseLong(timestamp.substring(0,2)+timestamp.substring(3,5));
    }

    private boolean checkIfOverlapByOne() {
        if (deimosRiseTimestamp == phobosRiseTimestamp) {
            return true;
        } else if (deimosRiseTimestamp == phobosSetTimestamp) {
            return true;
        } else if (deimosSetTimestamp == phobosRiseTimestamp) {
            return true;
        } else if (deimosSetTimestamp == phobosSetTimestamp) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNextDay(long start, long end) {
        if(start>end){
            return true;
        }
        return false;
    }
}
