package firstbig.com.example.first_big;

/**
 * Created by 짹짹 on 2014-09-15.
 */
public class CALMONEY {

    private final double[] basic_Money = {410, 910, 1600, 3850, 7300, 12940};
    private final double[] pay = {60.7, 125.9, 187.9, 280.6, 417.7, 709.5};
    private final double plus_Pay = 0.1;
    private final double industry_Pay = 0.037;

    public CALMONEY(){

    }

    public double getElecMoney(double elec_Data){
        double Money = 0;

        //전력량 요금 및 기본요금 계산
        if(elec_Data <= 100.0){
            Money = elec_Data * pay[0];
        }
        else if(elec_Data <= 200.0){
            Money = 100 * pay[0];

            elec_Data -= 100;
            Money += (elec_Data * pay[1]);
        }
        else if(elec_Data <= 300.0){
            for(int i=0;i<2;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 200;
            Money += elec_Data * pay[2];
        }
        else if(elec_Data <= 400.0){
            for(int i=0;i<3;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 300;
            Money += elec_Data * pay[3];
        }
        else if(elec_Data <= 500.0){
            for(int i=0;i<4;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 400;
            Money += elec_Data * pay[4];
        }
        else if(elec_Data > 500.0){
            for(int i=0;i<5;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 500;
            Money += elec_Data * pay[5];
        }

        // (기본요금 + 전력량 요금) 이 월 초저요금 1,000원보다 작을 경우 월 최저 요금 적용
//        if(Money < 1000){
//            Money = 1000;
//        }

        return Money;
    }

    public double getMoney(double elec_Data){

        double Money = 0;

        //전력량 요금 및 기본요금 계산
        if(elec_Data <= 100.0){
            Money = elec_Data * pay[0];
            Money += basic_Money[0];
        }
        else if(elec_Data <= 200.0){
            Money = 100 * pay[0];

            elec_Data -= 100;
            Money += (elec_Data * pay[1]);
            Money += basic_Money[1];
        }
        else if(elec_Data <= 300.0){
            for(int i=0;i<2;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 200;
            Money += elec_Data * pay[2];
            Money += basic_Money[2];
        }
        else if(elec_Data <= 400.0){
            for(int i=0;i<3;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 300;
            Money += elec_Data * pay[3];
            Money += basic_Money[3];
        }
        else if(elec_Data <= 500.0){
            for(int i=0;i<4;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 400;
            Money += elec_Data * pay[4];
            Money += basic_Money[4];
        }
        else if(elec_Data > 500.0){
            for(int i=0;i<5;i++){
                Money += (100 * pay[i]);
            }

            elec_Data -= 500;
            Money += elec_Data * pay[5];
            Money += basic_Money[5];
        }

        // (기본요금 + 전력량 요금) 이 월 초저요금 1,000원보다 작을 경우 월 최저 요금 적용
        if(Money < 1000){
            Money = 1000;
        }

        //부가가치세(원미만 4사 5입 == 반올림)
        double Plus = Math.round(Money * plus_Pay);
        //전력산업기반기금(10원미만 절사)
        double Industry = Math.floor((Money * industry_Pay) / 10.0) * 10.0;

        Money = Money + Plus + Industry;
        //10원 미만 절사
        Money = Math.floor(Money / 10.0) * 10;

       return Money;
    }

    public double getBasicMoney(double elec_Data){
        if(elec_Data > 500.0){
            return basic_Money[5];
        }
        else if(elec_Data > 400.0){
            return basic_Money[4];
        }
        else if(elec_Data > 300.0){
            return basic_Money[3];
        }
        else if(elec_Data > 200.0){
            return basic_Money[2];
        }
        else if(elec_Data > 100.0){
            return basic_Money[1];
        }
        else{
            return basic_Money[0];
        }
    }

    public double getPay(int i){
        return pay[i];
    }
}
