package firstbig.com.example.first_big;

/**
 * Created by 짹짹 on 2014-09-17.
 */
public class CALDAYMONEY {

    private CALMONEY mCalMoney;

    private double TotalElecData;
    private double TodayElecData;

    public CALDAYMONEY(double TotalElecData, double TodayElecData){
        this.TotalElecData = TotalElecData;
        this.TodayElecData = TodayElecData;

        mCalMoney = new CALMONEY();
    }

    public int getTodayMoney(){
        double TodayMoney;
        double TotalMoney;
        double remain;

        TotalMoney = mCalMoney.getElecMoney(TotalElecData);
        remain = mCalMoney.getElecMoney(TotalElecData - TodayElecData);

        TodayMoney = TotalMoney - remain;

        return (int)TodayMoney;
    }
}
