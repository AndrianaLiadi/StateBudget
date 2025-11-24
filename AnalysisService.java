public class AnalysisService {
    private float athroisma = 700000.30f;
    private int plhthos = 10;
    private float poso1 = 400.23f;
    private float poso2 = 500.77f;
    private float poso3 = 600.11f;
    private float esoda = 4000000.88f;
    private float exoda = 3000000.33f;
    public float mesosOros(float athroisma, int plhthos) {
        return athroisma/plhthos;
    }

    public float synola(float poso1, float poso2, float poso3) {
        return poso1 + poso2 + poso3;
    }

    public String sygkrish(float esoda, float exoda) {
        if (esoda > exoda) {
            return "pleonasmatikos";
        } else {
            return "elleimmatikos";
        }
    }

}
