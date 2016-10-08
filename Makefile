# A general java project makefile
# Author: Margular (2609135351@qq.com)
# create: 2016-10-07
# update: 2016-10-08
# version: 2.0

# set the filename of your jar package
JAR = Cknife.jar
# set the filename of manifest
MANIFEST = manifest.mf
# set your entry point of your java app
ENTRY_POINT = com.ms509.ui.Cknife
# -cp option for javac
CLASSPATH = lib/\*
# -d option for javac
DESTINATION = bin
# -sourcepath option for javac
SOURCE = src
# files you want to compile
SOURCE_FILE = $(subst .,/,$(ENTRY_POINT)).java
# set your resources directories
RES = com/ms509/images
# add extras dirs/files you want to purge
PURGE = Cknife.db Cknife.jar Config.ini
# set your java compiler here
JAVAC = javac
# set your java runtime environment here
JAVA = java

vpath %.java $(SOURCE)
vpath %.class $(DESTINATION)

.PHONY : all help run rebuild jar clean purge

all : $(DESTINATION) $(SOURCE_FILE:.java=.class)

help : 
	@echo "make [all]: build project."
	@echo "make run: run your app."
	@echo "make rebuild: rebuild project."
	@echo "make jar: package your project into a executable jar."
	@echo "make clean: clear classes generated."
	@echo "make purge: clean and purge additional files and dirs"

$(DESTINATION) : 
	mkdir -pv $(DESTINATION)

%.class : %.java
	$(JAVAC) -cp $(CLASSPATH) -d $(DESTINATION) -sourcepath $(SOURCE) $<

run : all
	$(JAVA) -cp $(DESTINATION):$(CLASSPATH):$(SOURCE) $(ENTRY_POINT)

rebuild : clean all

jar : all
	jar cvfm $(JAR) $(MANIFEST) -C $(DESTINATION) . -C $(SOURCE) $(RES)

clean :
	rm -rfv $(DESTINATION)

purge : clean
	rm -rfv $(PURGE)
