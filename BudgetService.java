public class BudgetService {
    private float originalFPA = 0.23f;
    private float originalbudget = 5000000.67f;
    private float originaltaktikos = 3000000.41f;
    private float originalependysewn = originalbudget - originaltaktikos;
    public float changeFPA(float originalFPA) {
        return originalFPA*2;
    }

    public float refresh(float originalbudget) {
        return originalbudget*2;
    }

    public float taktikos(float originaltaktikos) {
        return originaltaktikos*2;
    }

    public float ependysewn(float originalependysewn) {
        return originalependysewn*2;
    }

}
