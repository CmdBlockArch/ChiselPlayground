BUILD_DIR = ./build

PRJ = ChiselPlayground

test:
	mill ChiselPlayground.test

verilog:
	mkdir -p $(BUILD_DIR)
	mill ChiselPlayground.runMain Elaborate --target-dir $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR)

.PHONY: test verilog clean
