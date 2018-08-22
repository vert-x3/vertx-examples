###
# vert.x docker example using a Ruby verticle and a Gemfile for dependency downloads
# To build:
#  docker build -t sample/vertx-ruby-gemfile .
# To run:
#   docker run -t -i sample/vertx-ruby-gemfile
###

# Extend vert.x image                             <1>
FROM vertx/vertx3

# Set the name of the verticle to deploy          <2>
ENV VERTICLE_NAME hello-dependency-verticle.rb

# Set the location of the verticles               <3>
ENV VERTICLE_HOME /usr/verticles

# Copy your verticle to the container             <4>
COPY $VERTICLE_NAME $VERTICLE_HOME/

# config.json file that will be used in           <5>
# the --conf argument of 'exec vertx run'
# config file holds the GEM_PATH folder location
COPY config.json ${VERTICLE_HOME}/config.json

# -- RUBY GEMFILE INSTALL: START --
# Copy Gemfile into VERTICLE_HOME                 <6>
COPY Gemfile $VERTICLE_HOME/Gemfile

# Set Path for where bundler will get installed   <7>
# This is set before bundler install to prevent post-install warning by Bundler
ENV PATH=$PATH:/root/.gem/jruby/2.3.0/bin

# Install Bundler, Downloading Gems in Gemfile,   <8>
# and Print content of Gemfile.lock file
RUN java -jar ${VERTX_HOME}/lib/jruby-complete-9.1.13.0.jar -S gem install --user-install --no-document bundler \
 && BUNDLE_SILENCE_ROOT_WARNING=true java -jar ${VERTX_HOME}/lib/jruby-complete-9.1.13.0.jar -S bundle install --path=${VERTICLE_HOME}/bundle --gemfile=$VERTICLE_HOME/Gemfile \
 && cat $VERTICLE_HOME/Gemfile.lock 
# -- RUBY GEMFILE INSTALL: END --

# Launch the verticle                             <9>
# Notice the use of -conf config.json which 
# contains the GEM_PATH location
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* -conf config.json"]
