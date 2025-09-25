# Compilador Java
JAVAC = javac

# Diretório onde estão os ficheiros fonte
SRC_DIR = src

# Listar todos os ficheiros .java dentro de src/, model/, menus/ e utils/
SOURCES = $(wildcard $(SRC_DIR)/*.java) $(wildcard $(SRC_DIR)/model/*.java) $(wildcard $(SRC_DIR)/menus/*.java) $(wildcard $(SRC_DIR)/utils/*.java)

# Flags do compilador Java
JAVAC_FLAGS = -d $(SRC_DIR) -sourcepath $(SRC_DIR)

# Nome da classe principal do programa
MAIN_CLASS = Main

# Compilar todas as classes
all:
	$(JAVAC) $(JAVAC_FLAGS) $(SOURCES)

# Limpar arquivos compilados
clean:
	rm -f $(SRC_DIR)/*.class $(SRC_DIR)/model/*.class $(SRC_DIR)/menus/*.class $(SRC_DIR)/utils/*.class

# Executar o programa
run: all
	java -cp $(SRC_DIR) $(MAIN_CLASS)

.PHONY: all clean run