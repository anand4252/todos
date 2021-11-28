var JS_VENDOR_PATH = 'js/vendor',
    CSS_VENDOR_PATH = 'css/vendor';

module.exports = function(grunt) {

  grunt.loadNpmTasks('grunt-bowercopy');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-copy');

  grunt.initConfig({
    clean: {
      'vendor-js': JS_VENDOR_PATH,
      'vendor-css': CSS_VENDOR_PATH,
      'dist': 'dist'
    },

    bowercopy: {
        options: {
        },
        js: {
          options: {
            destPrefix: JS_VENDOR_PATH
          },
          files: {
            'todomvc-common.js' : 'todomvc-common/base.js',
            'jquery.js' : 'jquery/jquery.js',
            'underscore.js' : 'underscore/underscore.js',
            'backbone.js' : 'backbone/backbone.js'
          }
        },
        css: {
          options: {
            destPrefix: CSS_VENDOR_PATH
          },
          files: {
            'todomvc-common.css':'todomvc-common/base.css'
          }
        }
      },


      copy: {
        dist: {
	      files: [
          { src: 'index.html', dest: 'dist/static/' },
          {
            cwd: 'js',  // set working folder / root to copy
            src: '**/*',           // copy all files and subfolders
            dest: 'dist/static/js',    // destination folder
            expand: true           // required when using cwd
          },
          {
            cwd: 'css',  // set working folder / root to copy
            src: '**/*',           // copy all files and subfolders
            dest: 'dist/static/css',    // destination folder
            expand: true           // required when using cwd
          }

	      ]
	      }
      }

  });

  grunt.registerTask('default', ['clean','bowercopy', 'copy:dist']);

};
