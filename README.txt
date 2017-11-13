Guillermo Pilleux
19.605.689-0

github link

Solo se pueden ver los resultados de las carpetas (rey, pais, poem)
Para ver directamente los resultados ver el archivo results.txt de cada carpeta

EJECUTAR ESTE PRIMERO PARA VER RESULTADOS QUE YO ENCONTRÉ
Una ves generado el offspring ejecutar Evaluator.java
Al igual que en MainGuesser se pedirá especificar la carpeta del ejemplo que se desea ejectuar.
Lo que hace esta clase es tomar los datos necesarios (especificados en el informe) y sacará las relaciones entre cada palabra
Finalmente, entregará un reporte breve en results.txt

LUEGO CAMBIAR PARAMETROS PARA VER SU FUNCIONAMIENTO DEL ALGORITMO GENETICO

Buscar un nuevo offspring:
1ro: borrar el archivo offspring.txt de la carpeta del ejemplo que se desea ver
Se puede generar el offspring nuevamente
cambiando los parametros como cantidad de individuos en la poblacion o cantidad de iteraciones
En la clase MainGuesser.java precisamente en el WordGuesser wg = new WordGuesser(wordVecRep, <num_poblacion>, <dimension de vectores>)
cambiar la variable iterations para modificar las iteraciones que hará para encontrar un offspring.
Se tiene con 500k iteraciones para el cual encuentra buenos resultados con 10 individuos en la poblacion
