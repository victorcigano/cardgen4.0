Como rodar este projeto localmente (sem alterar o sistema)

Este repositório contém uma aplicação Spring Boot simples (CardGenerator) com frontend estático em `src/main/resources/static`.
As instruções abaixo mostram duas opções: usar um JDK já instalado no sistema ou utilizar um JDK portátil dentro do próprio workspace (sem alterar variáveis do Windows).

Resumo rápido
- URL da aplicação (quando subir): http://localhost:8080/
- Frontend: `src/main/resources/static/index.html` e `app.js` (já incluídos)
- Versão Java alvo: Java 17 (conforme `build.gradle`)

Opção A — se você já tem JDK 17 instalado (mais simples)
1. Abra um terminal PowerShell na raiz do projeto (a pasta que contém `gradlew.bat`).
2. Verifique a versão do Java:

```powershell
java -version
javac -version
```

3. Se o Java for >= 17, rode:

```powershell
.\gradlew.bat bootRun
```

4. Abra o navegador em http://localhost:8080/

Opção B — usar JDK portátil dentro do workspace (NÃO altera o sistema)

O que esta opção faz:
- Baixa e extrai um JDK (portable ZIP) para a pasta `.jdk` do workspace.
- Não altera `PATH` nem as variáveis de ambiente do Windows de forma permanente.
- Um task do VS Code (ou comandos PowerShell abaixo) aponta `JAVA_HOME` somente na sessão atual antes de rodar o `gradlew`.

Passos (PowerShell, execute na raiz do projeto):

```powershell
# 1) criar pasta .jdk
New-Item -ItemType Directory -Force -Path .\.jdk

# 2) baixar Temurin 17 (exemplo: release oficial). Se ocorrer erro de rede, baixe manualmente e coloque o ZIP em .\.jdk
$uri = "https://github.com/adoptium/temurin17-binaries/releases/latest/download/OpenJDK17U-jdk_x64_windows_hotspot.zip"
$zipPath = ".\.jdk\temurin17.zip"
Invoke-WebRequest -Uri $uri -OutFile $zipPath

# 3) extrair
Expand-Archive -LiteralPath $zipPath -DestinationPath .\.jdk -Force

# 4) renomear a pasta extraída para um nome estável (ajuste se o nome já existir)
$extracted = Get-ChildItem .\.jdk | Where-Object { $_.PSIsContainer -and $_.Name -ne 'jdk-17' } | Select-Object -First 1
if ($extracted) { Rename-Item -Path $extracted.FullName -NewName 'jdk-17' }

# 5) remover zip para economizar espaço
Remove-Item $zipPath

# 6) usar o JDK só na sessão e rodar a aplicação
$Env:JAVA_HOME = (Resolve-Path .\.jdk\jdk-17).Path
$Env:PATH = "$Env:JAVA_HOME\bin;$Env:PATH"
.\gradlew.bat bootRun
```

Observações importantes
- O `settings.json` do VS Code deste workspace já referencia `.jdk\\jdk-17.0.16+8`. Se você usar a opção B e renomear a pasta para `jdk-17`, o VS Code usará corretamente a runtime. Caso prefira manter a pasta com o nome do release (por exemplo `jdk-17.0.16+8`), ajuste as entradas em `.vscode/settings.json`.
- O front-end está em `src/main/resources/static/index.html` e `app.js`. Ao acessar http://localhost:8080/ o servidor Spring Boot serve esse conteúdo.
- O projeto usa Lombok (`compileOnly 'org.projectlombok:lombok'`), então no VS Code instale a extensão Lombok (ou a Java Extension Pack) para evitar avisos no editor. A compilação via Gradle funciona mesmo sem a extensão.
- Porta padrão: 8080 (pode ser alterada se você tiver `server.port` em `application.properties`, mas neste projeto não há configuração — portanto 8080 é usada).

Usar as tasks do VS Code (recomendado)
- Eu adicionei `.vscode/tasks.json` com 2 tarefas: `Gradle: bootRun` e `Gradle: bootRun (debug)`.
- A tarefa configura `JAVA_HOME` apenas para a execução e não altera o sistema.
- No VS Code: abra o Command Palette (Ctrl+Shift+P) → `Tasks: Run Task` → escolha `Gradle: bootRun`.

Debug (opcional)
- A tarefa `Gradle: bootRun (debug)` inicia a app com argumentos JVM para debug remoto na porta `5005`.
- Em seguida use a configuração de depuração `Attach to Spring Boot` (no painel Run/Debug) para anexar o depurador.

Resolução de problemas comuns
- Erro: `javac not found` ou `UnsupportedClassVersionError`: significa que o JDK em uso é anterior ao Java 17 — use a opção B ou instale JDK 17.
- Erro ao extrair/baixar o ZIP: verifique conexão e permissões. Você pode baixar manualmente no site https://adoptium.net/ e extrair em `.jdk`.
- Erro 403/404 ao baixar: o link `latest` pode redirecionar; baixe do site Adoptium se necessário.

Se quiser que eu baixe e execute automaticamente o JDK portátil aqui no workspace, me autorize (vou apenas colocar os arquivos em `.jdk` e rodar `gradlew` na sessão do terminal integrado do VS Code, sem tocar no sistema).

---
Versão curta de como rodar (se já tiver Java 17):

```powershell
.\gradlew.bat bootRun
# abrir http://localhost:8080/
```

---
Arquivos adicionados/alterados neste workspace:
- `.vscode/tasks.json` — tasks para rodar a aplicação (normal e debug)
- `.vscode/launch.json` — configuração para anexar o depurador
