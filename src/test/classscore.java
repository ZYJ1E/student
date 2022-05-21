package test;

public class classscore {
    public static final int CHIN=0;
    public static final int MATH=1;
    public static final int ENG=2;
    public static final int PHYS=3;
    public static final int CHEM=4;
    public static final int BIO=5;
    public static final int POL=6;
    public static final int HIS=7;
    public static final int GEO=8;
    public static final int ALL = 9;

    private double avgChin;
    private double avgMath;
    private double avgEng;
    private double avgChem;
    private double avgPhys;
    private double avgBio;
    private double avgPol;
    private double avgHis;
    private double avgGeo;
    private double avgAll;

    public classscore() {
    }

    public  double getSubjectAverage(int subject) {
        double res;

        switch (subject) {
            case MATH : {
                res = avgMath;
            }
            case CHIN: {
                res = avgChin;
            }
            case ENG: {
                res = avgEng;
            }
            case PHYS : {
                res = avgPhys;
            }
            case BIO:  {
                res = avgChem;
            }
            case CHEM:  {
                res = avgBio;
            }
            case POL:  {
                res = avgPol;
            }
            case HIS:  {
                res =avgHis;
            }
            case GEO:  {
                res =avgGeo;
            }
            default   : {
                res = avgAll;
            }
        }
        return res;
    }

    public void setAvgMath(double avgMath) {
        this.avgMath = avgMath;
    }
    public void setAvgChin(double avgChin) {
        this.avgChin = avgChin;
    }
    public void setAvgEng(double avgEng) {this.avgEng = avgEng;}
    public void setAvgPhys(double avgPhys) {this.avgPhys = avgPhys;}
    public void setAvgChem(double avgChem) {this.avgChem = avgChem;}
    public void setAvgBio(double avgBio) {this.avgBio = avgBio;}
    public void setAvgPol(double avgPol) {this.avgPol = avgPol;}
    public void setAvgHis(double avgHis) {this.avgHis = avgHis;}
    public void setAvgGeo(double avgGeo) {this.avgGeo = avgGeo;}
    public void setAvgAll(double avgAll) {this.avgAll = avgAll;}
}
