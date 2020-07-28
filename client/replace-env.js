const replace = require("replace-in-file");
const ENVS = require("../config/environment");
const STAGE = process.env.npm_config_stage;
const FILE_TO_REPLACE_ENV = "src/Env.elm"

main();

function main() {
  let currentEnv = ENVS[STAGE];

  for (key in currentEnv) {
    let value = currentEnv[key];
    replaceEnvCodeVariable(key, value);
  }
}

function replaceEnvCodeVariable(key, value) {
  try {
    const results = replace.sync({
      files: FILE_TO_REPLACE_ENV,
      from: new RegExp(`${key} = ".*"`, "g"),
      to: `${key} = "${value}"`,
    });
    console.log("Replacement results:", results);
  } catch (error) {
    console.error("Error occurred:", error);
  }
}
