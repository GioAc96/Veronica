const nunjucks = require('nunjucks')
const minifier = require('html-minifier')
const fs = require('fs')
const path = require('path')

const njEnv = new nunjucks.Environment(
  new nunjucks.FileSystemLoader('views')
)

const minifierOptions = {
  //collapseWhitespace: true
}

const pagesDir = path.join(__dirname, 'views/pages');
const outDir = path.join(__dirname, '..', 'docs');

const pages = fs.readdirSync(pagesDir)

for (const page of pages) {

  const pageOutput = minifier.minify(
    njEnv.render(path.join(pagesDir, page)),
    minifierOptions
  )

  fs.writeFileSync(
    path.join(outDir, page.replace('.njk', '.html')),
    pageOutput
  );

}