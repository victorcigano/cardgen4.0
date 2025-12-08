# Guia para Correção - Card Generator

Oi professor! 

Fiz esse guia rapidinho pra facilitar a correção do meu projeto. Tá tudo organizadinho aqui embaixo!

## Como Rodar o Projeto

```bash
# No terminal, dentro da pasta CardGeneratorVictorNicolauNeto:
gradlew bootRun

# Ou se preferir compilar primeiro:
gradlew build
gradlew bootRun
```

**Acesso:** http://localhost:8080

## Onde Encontrar Cada Requisito

### **5 Classes (5 pontos)**
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/Card.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/Banco.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/User.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/CardGenerator.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/CardValidator.java`

### **5 Controllers (4 pontos)**
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/controller/CardController.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/controller/AdminController.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/controller/BancoController.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/controller/UserController.java`
- `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/controller/ValidatorController.java`

### **Interface com 2 métodos (4 pontos)**
- **Interface:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/service/CardService.java`
  - Métodos: `gerarCard()` e `validarCard()`
- **Implementação:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/service/CardServiceImpl.java`
- **Uso:** No `CardController.java` (linhas 26, 48 e 51)

### **Agregação (4 pontos)**
- **Arquivo:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/Banco.java`
- **Linha 17:** `private List<Card> cartoes;` (Banco TEM cartões)

### **Associação (4 pontos)**
- **Arquivo:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/Card.java`
- **Linha 42:** `private User usuario;` (Card PERTENCE a um usuário)

### **Polimorfismo (4 pontos)**
- **Classe abstrata:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/Pagamento.java`
- **Implementação:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/model/PagamentoCartao.java`
- **Método abstrato:** `processar()` (linha 13 em Pagamento.java)

### **Application.properties (3 pontos)**
- **Arquivo:** `src/main/resources/application.properties`

### **Banco de dados - dialect e jdbc (4 pontos)**
- **Arquivo:** `src/main/resources/application.properties`
- **Linhas 5-7:** JDBC URL do H2
- **Linha 14:** Hibernate dialect

### **Dependências (15 pontos)**
- **Arquivo:** `build.gradle`
- **Linhas 27-33:** Spring Data JPA, Spring Web, Lombok

### **CRUD nos Controllers (15 pontos)**
Todos os controllers têm operações CRUD completas:
- **CREATE:** `@PostMapping`
- **READ:** `@GetMapping`
- **UPDATE:** `@PutMapping`
- **DELETE:** `@DeleteMapping`

### **Singleton nos controles (4 pontos)**
- **Arquivo:** `CardController.java`
- **Linhas 18 e 28-33:** Padrão Singleton implementado

### **Factory nos controles (4 pontos)**
- **Arquivo:** `CardController.java`
- **Linha 19:** `CardFactory.getInstance()`
- **Factory:** `src/main/java/com/sinodal/CardGeneratorVictorNicolauNeto/factory/CardFactory.java`

### **Try-catch nos controles (15 pontos)**
- **Arquivo:** `CardController.java`
- **Todos os métodos** têm blocos try-catch (linhas 36, 65, 76, 87, 98)

### **System.out.println nos controles (4 pontos)**
- **Arquivo:** `CardController.java`
- **Exemplos:** Linhas 38, 54, 67, 78, 89, 100

### **Logger.debug nos controles (4 pontos)**
- **Arquivo:** `CardController.java`
- **Exemplos:** Linhas 39, 55, 68, 79, 90, 101

## Como Testar

### 1. Cadastrar Usuário:
```bash
curl -X POST "http://localhost:8080/api/users/cadastrar" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Victor Nicolau","email":"victor@email.com","login":"victor","senha":"123456"}'
```

### 2. Fazer Login:
```bash
curl -X POST "http://localhost:8080/api/users/login" \
  -H "Content-Type: application/json" \
  -d '{"login":"victor","senha":"123456"}'
```

### 3. Listar Bandeiras Disponíveis:
```bash
curl -X GET "http://localhost:8080/api/cartoes/bandeiras"
```

### 4. Gerar Cartão (Visa por padrão):
```bash
curl -X POST "http://localhost:8080/api/cartoes/gerar?nomeTitular=Victor"
```

### 5. Gerar Cartão com Bandeira Específica:
```bash
# MasterCard
curl -X POST "http://localhost:8080/api/cartoes/gerar?nomeTitular=Victor&bandeira=MasterCard"

# Elo
curl -X POST "http://localhost:8080/api/cartoes/gerar?nomeTitular=Victor&bandeira=Elo"

# American Express
curl -X POST "http://localhost:8080/api/cartoes/gerar?nomeTitular=Victor&bandeira=American%20Express"
```

### 6. Listar Cartões:
```bash
curl -X GET "http://localhost:8080/api/cartoes"
```

### 7. Listar Usuários:
```bash
curl -X GET "http://localhost:8080/api/users"
```

### Console H2 (para ver o banco):
- Acesse: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:carddb`
- User: `sa`
- Password: (deixe vazio)

## Funcionalidades Implementadas

### Sistema de Login:
- **Cadastro:** `/api/users/cadastrar` - Cria novo usuário com login e senha
- **Login:** `/api/users/login` - Autentica usuário e atualiza último login
- **Validação:** Verifica se login já existe no cadastro
- **Persistência:** Dados salvos no banco H2

### Geração de Cartões:
- **Bandeiras:** Visa, MasterCard, American Express, Elo, Hipercard
- **Algoritmo de Luhn:** Garante números válidos (múltiplos de 10)
- **Laço de validação:** Regenera até obter número válido
- **Prefixos corretos:** Cada bandeira tem seus prefixos específicos
- **Escolha de bandeira:** Parâmetro opcional (padrão: Visa)

## Observações

- O projeto roda na porta 8080
- Usa banco H2 em memória (dados são perdidos ao reiniciar)
- Todos os logs aparecem no console
- Sistema de login simples implementado
- Postman collection atualizada: `postman_collection_cardgenerator.json`

---

**Espero que esteja tudo certinho, professor!**

Se tiver alguma dúvida, é só me chamar!

*Victor Nicolau Neto*