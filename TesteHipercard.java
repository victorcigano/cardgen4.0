public class TesteHipercard {
    public static void main(String[] args) {
        System.out.println("=== TESTE GERAÇÃO HIPERCARD ===");
        
        String hipercard = gerarHipercard();
        System.out.println("Número gerado: " + hipercard);
        System.out.println("Tamanho: " + hipercard.length());
        System.out.println("Prefixo 6062: " + hipercard.startsWith("6062"));
        System.out.println("Válido pelo Luhn: " + luhnCheck(hipercard));
        
        // Verificar se o dígito verificador está correto
        String base = hipercard.substring(0, 15);
        int digitoCalculado = calcularDigitoLuhn(base);
        int digitoAtual = Character.getNumericValue(hipercard.charAt(15));
        
        System.out.println("\nBase (15 dígitos): " + base);
        System.out.println("Dígito calculado: " + digitoCalculado);
        System.out.println("Dígito atual: " + digitoAtual);
        System.out.println("Dígitos coincidem: " + (digitoCalculado == digitoAtual));
    }
    
    public static String gerarHipercard() {
        StringBuilder numero = new StringBuilder("6062");
        
        // Gerar 11 dígitos aleatórios (15 - 4 do prefixo - 1 do verificador)
        for (int i = 0; i < 11; i++) {
            numero.append((int)(Math.random() * 10));
        }
        
        String base = numero.toString();
        int checkDigit = calcularDigitoLuhn(base);
        
        return base + checkDigit;
    }
    
    private static int calcularDigitoLuhn(String numero) {
        int sum = 0;
        boolean alternate = true; // Começar com true pois será o próximo dígito
        
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