## Documentación de la API RESTful
Ingresa a la ruta ```/swagger-ui/index.html#/``` para poder ver la documentación de la API e incluso realizar request desde ahí mismo

## Pasos para iniciar el proyecto

1. Clona este repositorio:

Ingresa a la ruta donde descargar el proyecto con Power Shell

```bash
git clone https://github.com/RafaelSolier/Proyecto-DS.git
```
2. En el terminal crea nuevo contenedor:

```bash
docker run -p 5555:5432 --name e2e-postgres -e POSTGRES_PASSWORD=123 -d postgres
```
3. En nuevo terminal donde se encuentre el archivo cargar datos:

```bash
docker cp cargar_datos.txt e2e-postgres:/cargar_datos.txt
```
Deberia salir algo como:
```bash
Successfully copied 8.19kB to e2e-postgres:/cargar_datos.txt
```
4. Ejecuta el programa para crear las tablas, luego accede al contendor y carga datos
```bash
docker exec -it e2e-postgres psql -U postgres
\i /cargar_datos.txt
```

5. En el navegador verificar:

```bash
http://localhost:8080/actuator
```

6. Ingresar a la ruta del frontend y en el terminal:

```bash
npm install 
npm run dev 
```
