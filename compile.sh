compile:
	@rm -rf classes
	@mkdir classes
	@rm -rf mods
	@mkdir mods
	@echo " > copy content of libs"
	@cp libs/* mods
	@echo " > creating crypto"
	@javac --module-path mods -d classes/crypto \
	$(find crypto -name '*.java')
