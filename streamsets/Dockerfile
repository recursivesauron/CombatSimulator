FROM streamsets/datacollector:3.21.0
ENV SDC_LIBS=streamsets-datacollector-apache-kafka_2_6-lib,streamsets-datacollector-influxdb_0_9-lib
USER root
RUN if [ -n "${SDC_LIBS}" ]; then "${SDC_DIST}/bin/streamsets" stagelibs -install="${SDC_LIBS}"; fi
USER ${SDC_USER}
EXPOSE 18630
COPY ./docker-entrypoint.sh /
RUN dos2unix /docker-entrypoint.sh
COPY ./form-realm.properties ${SDC_CONF}/
ENTRYPOINT ["./docker-entrypoint.sh"]
CMD ["dc", "-exec"]