public class Vanillon {

    public static int getDesiccant(int arrowed, int nayword) {

        if (arrowed == nayword) {

            int diafora = arrowed - nayword;
            int tetragono = diafora * diafora;
            int result = Math.abs(tetragono);
            return result;

        } else {

            int sum = arrowed + nayword;
            int result = sum * sum;
            return result;

        }
    } public static void main(String[]args) {
        int finalresult = getDesiccant(16, 37);
        int finalresult2 = getBehemoth(4, 58, 37);
    }
    public static int getBehemoth(int solicitor, int haggatic, int diatropic) {

        int i = 1;
        while (i <= 15) {
            haggatic = haggatic + solicitor;
            i++;
        }
        int sweeting = haggatic;
        int xan = sweeting * sweeting;
        if (xan < diatropic) {
            int res = sweeting;
            return res;
        } else {
            int res = -sweeting;
            return res;
        }
    }
}
