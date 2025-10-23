# 🚀 Deploy CardGenerator

## Opções de Deploy Gratuito

### 1. **Railway** (Recomendado)
```bash
# 1. Criar conta em railway.app
# 2. Conectar repositório GitHub
# 3. Deploy automático com railway.json
```

### 2. **Render**
```bash
# 1. Criar conta em render.com
# 2. Conectar repositório GitHub  
# 3. Deploy automático com render.yaml
```

### 3. **Heroku**
```bash
# 1. Instalar Heroku CLI
# 2. Login e criar app
heroku login
heroku create cardgenerator-app
git push heroku main
```

### 4. **Docker Local**
```bash
# Build da imagem
docker build -t cardgenerator .

# Executar container
docker run -p 8080:8080 cardgenerator
```

## ⚙️ Configurações

- **Java 17** configurado
- **Porta dinâmica** ${PORT:8080}
- **H2 Database** em memória
- **Arquivos estáticos** incluídos

## 🌐 URLs de Acesso

Após deploy:
- **Login:** `/login.html`
- **App:** `/`
- **Admin:** Botão no navbar

## 📝 Notas

- Banco H2 em memória (dados resetam a cada deploy)
- Para produção, considere PostgreSQL
- CORS configurado para localhost (ajustar para domínio real)