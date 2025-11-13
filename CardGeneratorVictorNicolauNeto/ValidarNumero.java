public class ValidarNumero {
    public static void main(String[] args) {
        String numero = "6062181993672590"; // Removendo espaços
        
        System.out.println("Número: " + numero);
        System.out.println("Tamanho: " + numero.length());
        System.out.println("Prefixo Hipercard (6062): " + numero.startsWith("6062"));
        System.out.println("Algoritmo de Luhn: " + luhnCheck(numero));
        
        // Verificação manual do Luhn
        System.out.println("\n=== VERIFICAÇÃO MANUAL ===");
        verificarLuhnManual(numero);
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
    
    private static void verificarLuhnManual(String numero) {
        System.out.println("Dígitos da direita para esquerda:");
        int sum = 0;
        boolean alt = false;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numero.substring(i, i + 1));
            int original = n;
            
            if (alt) {
                n *= 2;
                if (n > 9) n = n - 9;
            }
            
            System.out.printf("Posição %d: %d -> %d\n", numero.length() - i, original, n);
            sum += n;
            alt = !alt;
        }
        
        System.out.println("Soma total: " + sum);
        System.out.println("Soma % 10: " + (sum % 10));
        System.out.println("Válido: " + (sum % 10 == 0));
    }
}