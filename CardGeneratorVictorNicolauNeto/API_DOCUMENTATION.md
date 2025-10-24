# 📋 API Documentation - CardGenerator

## 🚀 Visão Geral
API REST para geração de cartões de crédito fictícios para desenvolvimento e testes.

## 🔗 Base URL
```
http://localhost:8080
```

## 🔐 Autenticação
A API utiliza headers customizados para autenticação:
- `X-CG-USER`: Nome do usuário (obrigatório)
- `X-CG-ADMIN`: Token de admin (apenas para endpoints administrativos)

## 📚 Endpoints

### 🎯 Cards API

#### POST /cards/gerar
Gera um novo cartão de crédito fictício.

**Headers:**
```
X-CG-USER: string (obrigatório)
Content-Type: application/json
```

**Query Parameters:**
- `nomeTitular` (opcional): Nome do portador do cartão

**Response 200:**
```json
{
  "numero": "1234567890123456",
  "nomeTitular": "João Silva",
  "validade": "2025-12",
  "cvv": 123,
  "bandeira": "Visa",
  "teste": true
}
```

**Errors:**
- `401`: User registration required
- `400`: Name too long (max 100 characters)
- `500`: Failed to generate valid card

---

#### GET /cards/listar
Lista todos os cartões gerados.

**Response 200:**
```json
[
  {
    "numero": "1234567890123456",
    "nomeTitular": "João Silva",
    "validade": "2025-12",
    "cvv": 123,
    "bandeira": "Visa",
    "teste": true
  }
]
```

---

#### DELETE /cards/{numero}
Remove um cartão específico.

**Headers:**
```
X-CG-USER: string (obrigatório)
```

**Path Parameters:**
- `numero`: Número do cartão (16 dígitos)

**Response:**
- `204`: Card deleted successfully
- `401`: User registration required
- `400`: Card number is required
- `404`: Card not found

---

#### DELETE /cards
Remove todos os cartões.

**Headers:**
```
X-CG-USER: string (obrigatório)
```

**Response:**
- `204`: All cards deleted successfully
- `401`: User registration required

### 🛡️ Admin API

#### POST /admin/auth
Autentica usuário como administrador.

**Headers:**
```
X-CG-USER: string (obrigatório)
Content-Type: application/json
```

**Body:**
```json
{
  "password": "admin123"
}
```

**Response 200:**
```json
{
  "message": "Authentication successful"
}
```

**Errors:**
- `401`: User authentication required
- `400`: Password is required
- `403`: Invalid credentials

---

#### GET /admin/cards
Lista todos os cartões (visão administrativa).

**Headers:**
```
X-CG-USER: string (obrigatório)
X-CG-ADMIN: authenticated (obrigatório)
```

**Response 200:**
```json
[
  {
    "numero": "1234567890123456",
    "nomeTitular": "João Silva",
    "validade": "2025-12",
    "cvv": 123,
    "bandeira": "Visa",
    "teste": true
  }
]
```

---

#### DELETE /admin/cards/{numero}
Remove um cartão específico (admin).

**Headers:**
```
X-CG-USER: string (obrigatório)
X-CG-ADMIN: authenticated (obrigatório)
```

**Response:**
- `204`: Card deleted successfully
- `401`: User registration required
- `403`: Admin authentication required
- `404`: Card not found

---

#### DELETE /admin/cards
Remove todos os cartões (admin).

**Headers:**
```
X-CG-USER: string (obrigatório)
X-CG-ADMIN: authenticated (obrigatório)
```

**Response:**
- `204`: All cards deleted successfully
- `401`: User registration required
- `403`: Admin authentication required

## 🎨 Frontend

### Páginas Disponíveis
- `/` - Aplicação principal
- `/login.html` - Página de login
- `/h2-console` - Console do banco H2 (desenvolvimento)

## 🔧 Configuração

### Variáveis de Ambiente
- `PORT`: Porta do servidor (padrão: 8080)

### Banco de Dados
- **Tipo**: H2 (file-based)
- **Localização**: `./data/carddb`
- **Console**: http://localhost:8080/h2-console
- **URL JDBC**: `jdbc:h2:file:./data/carddb`
- **Usuário**: `sa`
- **Senha**: (vazio)

## 🧪 Validações

### Cartão
- **Número**: Exatamente 16 dígitos, validado com algoritmo de Luhn
- **Nome**: Máximo 100 caracteres, obrigatório
- **CVV**: Entre 100 e 999
- **Validade**: Deve ser futura
- **Bandeira**: Obrigatória (Visa, MasterCard, Elo, American Express, Hipercard)

### Segurança
- Headers customizados para autenticação
- Validação de entrada em todos os endpoints
- CORS configurado para localhost
- Sanitização de dados no frontend

## 📊 Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | Sucesso |
| 204 | Sucesso sem conteúdo |
| 400 | Requisição inválida |
| 401 | Não autorizado |
| 403 | Proibido |
| 404 | Não encontrado |
| 500 | Erro interno do servidor |

## 🚀 Como Executar

1. **Pré-requisitos**: Java 17+
2. **Executar**: `./gradlew bootRun`
3. **Acessar**: http://localhost:8080

## 🧪 Testes

Execute os testes com:
```bash
./gradlew test
```

## 📝 Notas Importantes

- ⚠️ **Apenas para desenvolvimento**: Cartões gerados são fictícios
- 🔒 **Senha admin padrão**: `admin123`
- 💾 **Dados persistentes**: Armazenados em `./data/carddb`
- 🎯 **Algoritmo de Luhn**: Números de cartão válidos matematicamente