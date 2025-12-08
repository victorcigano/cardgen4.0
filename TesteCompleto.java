public class TesteCompleto {
    public static void main(String[] args) {
        System.out.println("=== TESTE ALGORITMO DE LUHN COMPLETO ===\n");
        
        // Teste Visa
        String visa = gerarCartaoVisa();
        System.out.println("VISA: " + visa);
        System.out.println("Válido: " + luhnCheck(visa));
        System.out.println("Prefixo correto: " + visa.startsWith("4"));
        System.out.println();
        
        // Teste MasterCard
        String master = gerarCartaoMasterCard();
        System.out.println("MASTERCARD: " + master);
        System.out.println("Válido: " + luhnCheck(master));
        System.out.println("Prefixo correto: " + validarPrefixoMaster(master));
        System.out.println();
        
        // Teste American Express
        String amex = gerarCartaoAmex();
        System.out.println("AMEX: " + amex);
        System.out.println("Válido: " + luhnCheck(amex));
        System.out.println("Prefixo correto: " + (amex.startsWith("34") || amex.startsWith("37")));
        System.out.println("Tamanho correto: " + (amex.length() == 15));
    }
    
    public static String gerarCartaoVisa() {
        StringBuilder numero = new StringBuilder("4");
        for (int i = 0; i < 14; i++) {
            numero.append((int)(Math.random() * 10));
        }
        String base = numero.toString();
        return base + calcularDigitoLuhn(base);
    }
    
    public static String gerarCartaoMasterCard() {
        String[] prefixos = {"51", "52", "53", "54", "55"};
        String prefixo = prefixos[(int)(Math.random() * prefixos.length)];
        StringBuilder numero = new StringBuilder(prefixo);
        for (int i = 0; i < 13; i++) {
            numero.append((int)(Math.random() * 10));
        }
        String base = numero.toString();
        return base + calcularDigitoLuhn(base);
    }
    
    public static String gerarCartaoAmex() {
        String[] prefixos = {"34", "37"};
        String prefixo = prefixos[(int)(Math.random() * prefixos.length)];
        StringBuilder numero = new StringBuilder(prefixo);
        for (int i = 0; i < 12; i++) {
            numero.append((int)(Math.random() * 10));
        }
        String base = numero.toString();
        return base + calcularDigitoLuhn(base);
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
    
    private static boolean validarPrefixoMaster(String numero) {
        return numero.startsWith("51") || numero.startsWith("52") || 
               numero.startsWith("53") || numero.startsWith("54") || 
               numero.startsWith("55");
    }
}