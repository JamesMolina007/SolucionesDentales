import subprocess

# Ruta al archivo autowhat.js
ruta_archivo_js = "C:/SolucionesDentales/AutoMessage/autoexe.bat"

# Ejecutar el archivo JavaScript usando Node.js
comando = f"{ruta_archivo_js}"
subprocess.run(comando, shell=True)
