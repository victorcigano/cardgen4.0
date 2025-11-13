public class TesteLuhn {
    public static void main(String[] args) {
        // Teste do gerador
        String numero = gerarNumero();
        System.out.println("Número gerado: " + numero);
        System.out.println("É válido? " + luhnCheck(numero));
        
        // Teste com número conhecido válido
        String numeroValido = "4532015112830366";
        System.out.println("Número válido conhecido: " + numeroValido);
        System.out.println("É válido? " + luhnCheck(numeroValido));
    }
    
    public static String gerarNumero() {
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            numero.append((int)(Math.random() * 10));
        }
        
        String base = numero.toString();
        int checkDigit = calcularDigitoLuhn(base);
        
        return base + checkDigit;
    }
    
    private static int calcularDigitoLuhn(String numero) {
        int sum = 0;
        boolean alternate = true;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numero.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (10 - (sum % 10)) % 10;
    }
    
    private static boolean luhnCheck(String ccNumber) {
        int sum = 0;
        boolean alt = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alt) {
                n *= 2;
                if (n > 9) n = n - 9;
            }
            sum += n;
            alt = !alt;
        }
        return (sum % 10 == 0);
    }
}