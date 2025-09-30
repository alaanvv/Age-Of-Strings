#!/bin/sh

# ==============================================================================
# git_blame_analyzer_compatible.sh
#
# Versão compatível com qualquer shell (POSIX), sem usar recursos do Bash.
# Analisa o repositório, conta as linhas por autor e exibe o relatório.
# ==============================================================================

# Garante que o script pare se algum comando falhar
set -e

echo "Passo 1: Verificando se este é um repositório Git..."
if [ ! -d .git ]; then
    echo "Erro: Este diretório não parece ser um repositório Git."
    exit 1
fi
echo "Repositório Git encontrado."

echo "Passo 2: Analisando a autoria de todos os arquivos (isso pode levar um tempo)..."

# --- O NOVO CORAÇÃO DO SCRIPT (SEM ARRAYS) ---
# 1. Gera a lista de autores, uma por linha, como antes.
# 2. 'sort': Agrupa todos os nomes de autores idênticos em sequência.
# 3. 'uniq -c': Conta as ocorrências de cada linha consecutiva (por isso o 'sort' antes é essencial).
#    A saída será no formato " <contagem> <nome_do_autor>".
# 4. 'sort -rn': Ordena o resultado numericamente (-n) e em ordem reversa (-r),
#    colocando os maiores contribuidores no topo.
# O resultado final é salvo na variável 'author_stats'.
#------------------------------------------------
author_stats=$(git ls-files -z | xargs -0 git blame -w --line-porcelain 2>/dev/null | grep "^author " | sed 's/^author //' | sort | uniq -c | sort -rn)

# Para calcular o total, usamos 'awk' para somar a primeira coluna (a contagem) das estatísticas.
total_lines=$(echo "$author_stats" | awk '{s+=$1} END {print s}')

# Se o total for zero, pode ser um valor nulo, então garantimos que seja '0'
if [ -z "$total_lines" ]; then
    total_lines=0
fi

echo "\n--- Análise do Git Blame Concluída ---"
echo "Total de linhas de código analisadas: $total_lines"

if [ "$total_lines" -eq 0 ]; then
    echo "Nenhuma linha foi encontrada para análise."
    exit 0
fi

# Cabeçalho da tabela de resultados
echo ""
printf "%-30s %10s %15s\n" "Autor" "Linhas" "Percentual (%)"
echo "---------------------------------------------------------" # Linha de separação sem bashismos

# --- GERAÇÃO DO RELATÓRIO ---
# Usamos 'echo' para passar as estatísticas para um loop 'while read'.
# O loop lê a contagem na variável 'count' e o resto da linha (o nome) na variável 'author'.
#-------------------------------
echo "$author_stats" | while read -r count author; do
    # 'bc' continua sendo usado para o cálculo com casas decimais.
    percentage=$(echo "scale=2; ($count * 100) / $total_lines" | bc)

    # 'printf' para formatar a saída. Usamos %s para o percentual, pois 'bc' retorna uma string.
    printf "%-30s %10s %15s%%\n" "$author" "$count" "$percentage"
done