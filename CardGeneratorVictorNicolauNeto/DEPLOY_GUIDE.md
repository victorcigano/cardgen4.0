# 🚀 Guia de Deploy - CardGenerator

## 🌐 Opções de Deploy Gratuitas

### 1. **Render** (Recomendado)
1. Acesse: https://render.com
2. Conecte sua conta GitHub
3. Clique em "New Web Service"
4. Conecte este repositório
5. Configure:
   - **Build Command**: `./gradlew build -x test`
   - **Start Command**: `java -Dserver.port=$PORT -jar build/libs/*.jar`
   - **Environment**: Java 17

### 2. **Railway**
1. Acesse: https://railway.app
2. Conecte GitHub
3. Deploy from GitHub repo
4. Configuração automática

### 3. **Heroku**
1. Instale Heroku CLI
2. Execute:
```bash
heroku create cardgenerator-victor
git push heroku main
```

## 🔧 Configurações Necessárias

### Variáveis de Ambiente
- `PORT`: Porta do servidor (automática)
- `JAVA_TOOL_OPTIONS`: `-XX:MaxRAMPercentage=75.0`

### Arquivos de Deploy
- ✅ `render.yaml` - Configuração Render
- ✅ `Dockerfile` - Container Docker
- ✅ `Procfile` - Heroku

## 🎯 URLs Após Deploy
- **Aplicação**: `https://seu-app.onrender.com`
- **Admin**: `https://seu-app.onrender.com` (senha: admin123)
- **API**: `https://seu-app.onrender.com/cards/listar`

## 📝 Passos Rápidos

1. **Commit e Push**:
```bash
git add .
git commit -m "Deploy configuration"
git push origin main
```

2. **Deploy no Render**:
   - Conectar repositório
   - Deploy automático

3. **Testar**:
   - Acessar URL fornecida
   - Testar funcionalidades

## 🔒 Segurança em Produção
- Banco H2 em memória (dados temporários)
- HTTPS automático
- Headers de segurança configurados