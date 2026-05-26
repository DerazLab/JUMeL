inicio-programa

variables: n, prom, i, cal, j

escribir "Cuantas calificaciones ingresara"
leer n

prom = 0
i = 0

mientras (i < n)
    escribir "Da una calificacion"
    leer cal
    prom = prom + cal
    i = i + 1
fin-mientras

prom = prom / n

si prom > 5 entonces
    escribir "Aprobado con ", prom
fin-si

si prom == 10 entonces
    escribir "Excelente"
fin-si

escribir "Probando ciclo repite"

repite (j, 1, 3)
    escribir "Iteracion numero ", j
fin-repite

fin-programa