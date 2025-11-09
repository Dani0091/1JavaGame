# Nuevo Repositorio de Game to Fork (EDD) :computer:
**[ES]**
Este repositorio es un proyecto de JavaFX para practicar la creación y modificación de un repositorio propio a partir de una base.

## Proyecto
En primer lugar he instalado y configurado [GitKraken](https://www.gitkraken.com/) para poder trabajar. Después, he creado desde la interfaz web de GitHub un nuevo repositorio para la nueva bifurcación.

1. **Crear repositorio vacío en GitHub**  
   ![Captura de la creación del repositorio nuevo en GitHub](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/Crear_Nuevo_Repositorio.png)

2. **Clonar el repositorio con GitKraken**  
   ![Captura de la sección para clonar un repositorio en GitKraken](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/Clonar_Repo.png)

3. **Crear una nueva rama con GitKraken**  
   ![Captura de la creación de una nueva rama en GitKraken](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/NewBranchName.png)

4. **Cambios en el juego: fondo y explosión de partículas**  
   He cambiado el fondo del videojuego y añadido un efecto de explosión con partículas al hacer clic sobre un círculo.  
   | Cambio | Captura |
   |--------|---------|
   | **Fondo nuevo (negro)** | ![Fondo nuevo del juego en negro](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/Fondo_Nuevo_negro.png) |
   | **Explosión de partículas** | ![Efecto de partículas al hacer clic en un círculo](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/Particulas.png) |

5. **Commits realizados en la nueva rama**  
   ![Captura de un commit desde la interfaz de GitKraken](https://raw.githubusercontent.com/Dani0091/A-Game-to-Fork/NewDaniBranch/img/Commit_Desde_GitKraken.png)

6. **Fusionar la rama `NewDaniBranch` en el repositorio nuevo vacío (`1JavaGame`)**  
   El objetivo era inicializar el repositorio vacío con toda la historia y cambios de mi rama.  
   Aunque intenté hacerlo gráficamente en GitKraken, el *upstream* previo (`Dani0091`) impedía cambiar el remote en el botón **Push**.

   ### Solución: Terminal integrada de GitKraken  
   Abrí la terminal (`>_`) y ejecuté estos comandos:

   ```bash
   # Subir mi branch como 'main' al nuevo repositorio vacío
   git push 1JavaGame NewDaniBranch:main

   # Crear también la rama de desarrollo en el nuevo repo
   git push 1JavaGame NewDaniBranch:NewDaniBranch

   # Configurar upstream para futuros pushes
   git branch --set-upstream-to=1JavaGame/main NewDaniBranch

   ## Uso de la IA generativa

En este caso se ha utilizado:

- **Anthropic (2025).** _Claude_ (Sonnet 3.5) [LLM]  
  [Claude](https://claude.ai/chat/)

- **Prompt utilizado para mejorar el juego:**

```text
Puedes ayudarme con esto: Quiero Modificar y añadir efecto de particulas a los circulos en el juego de este repositorio:
https://github.com/Dani0091/A-Game-to-Fork, ¿que métodos tengo que modificar?
