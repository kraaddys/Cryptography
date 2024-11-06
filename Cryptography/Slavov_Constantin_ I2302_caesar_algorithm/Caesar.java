public class Caesar {
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static char symbol_right_shift(char symbol, int shift){
        if(alphabet.indexOf(symbol) != -1){
            return alphabet.charAt((alphabet.indexOf(symbol) + shift) % alphabet.length());
        }
        else {
            return symbol;
        }
    }

    public static void main(String[] args) {
        String text = "Hello everyone my name is Kostya";
        text = text.toLowerCase();
        for(int i = 0; i <= 26; ++i){
            for(int j = 0; j < text.length(); ++j) {
                System.out.print(symbol_right_shift(text.charAt(j), i));
            }
            System.out.println();
        }
    }
}
