# Require any additional compass plugins here.

# Set this to the root of your project when deployed:
http_path = "/"
css_dir = "war/css"
sass_dir = "scss"
images_dir = "images"
javascripts_dir = "js"

http_stylesheets_path = "/css"

sass_options = {
  :precision => 5,
  :sourcemap => true
}

enable_sourcemaps = true

# You can select your preferred output style here (can be overridden via the command line):
# output_style = :expanded or :nested or :compact or :compressed
output_style = (environment == :production) ? :compressed : :nested

# To enable relative paths to assets via compass helper functions. Uncomment:
# relative_assets = true

# To disable debugging comments that display the original location of your selectors. Uncomment:
# line_comments = false
