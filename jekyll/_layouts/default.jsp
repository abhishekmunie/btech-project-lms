{% include jspimports.jsp %}
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>{{ site.title_prefix }}{% if page.title and site.title_prefix %} &middot; {% endif %}{{ page.title }}{% if page.title and site.title_suffix %} &middot; {% endif %}{{ site.title_suffix }}</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

    {% for css in site.imports.css %}<link rel="stylesheet" href="{% if css[1].url %}{{ css[1].url }}{% else %}{{ css[1] }}{% endif %}" {% if css[1].media %} media="{{ css[1].media }}"{% endif %}/>
    {% endfor %}

    {% if site.imports.modernizr %}<script src="{{ site.imports.modernizr }}" type="text/javascript"></script>{% endif %}
    <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="/bower_components/jquery/dist/jquery.min.js"><\/script>')</script> -->
    <script src="/bower_components/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript">
      importScript = function(src, defer, id, callback) {
        var sp = document.createElement('script');
        sp.type = "text/javascript";
        sp.src = src;
        sp.async = true;
        id && (sp.id = id);
        defer && (sp.defer = "defer");
        callback && (sp.onload = sp.onreadystatechange = function() {
          var rs = this.readyState;
          if(rs && rs != 'complete' && rs != 'loaded') return;
          try{ callback(); } catch(e) {console.error(e)}
        });
        var s=document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(sp,s);
      };

      {% for js in site.imports.js %}
      importScript('{% if js[1].url %}{{ js[1].url }}{% else %}{{ js[1] }}{% endif %}',
        {% if js[1].defer %}{{ js[1].defer }}{% else %}false{% endif %},
       '{% if js[1].id %}{{ js[1].id }}{% else %}js-{{ js[0] }}{% endif %}',
        {% if js[1].callback %}{{ js[1].callback }}{% else %}null{% endif %});
      {% endfor %}
    </script>
  </head>
  <body>
    {% assign levels = page.url | split: '/' %}

    {% include warnings.jsp %}
    {% include nav.jsp %}
    {% include breadcrumbs.html %}

    {{ content }}

    <!-- FOOTER -->
    <footer id="footer">
      <div class="container text-center">
        <a href="https://developers.google.com/cloud" title="Google Cloud Platform" class="pull-left">
          <img src="/images/google-cloud-small.png" target="_blank" /><br />
          <p>Powered by Google Cloud Platform</p>
        </a>
        <p class="pull-right">&copy; 2014 {{site.company}} &middot; <a href="/privay">Privacy</a> &middot; <a href="/terms">Terms</a></p>
        <a href="https://apple.com/mac" class="text-center">
          <i class="fa fa-apple fa-2x"></i><br />
          Created on a Mac
        </a>
      </div>
    </footer>

    {% if config.google.analytics %}
    <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
    <script>
      (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
      function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
      e=o.createElement(i);r=o.getElementsByTagName(i)[0];
      e.src='//www.google-analytics.com/analytics.js';
      r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
      ga('create','{{site.config.google.analytics.id}}');ga('send','pageview');
    </script>
    {% endif %}
  </body>
</html>
