BUILD_DIR = ./build

PRJ = ChiselPlayground

idea:
	mill -i mill.idea.GenIdea/idea

verilog:
	mkdir -p $(BUILD_DIR)
	mill ChiselPlayground.runMain Elaborate --target-dir $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR)

.PHONY: test verilog clean
