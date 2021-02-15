FROM streamsets/datacollector:latest
ENV SDC_LIBS=streamsets-datacollector-apache-kafka_2_0-lib,streamsets-datacollector-influxdb_0_9-lib
USER root
RUN if [ -n "${SDC_LIBS}" ]; then "${SDC_DIST}/bin/streamsets" stagelibs -install="${SDC_LIBS}"; fi
USER ${SDC_USER}
EXPOSE 18630
COPY ./docker-entrypoint.sh /
RUN ls -la /docker-entrypoint.sh
COPY ./form-realm.properties ${SDC_CONF}/
ENTRYPOINT ["sh", "./docker-entrypoint.sh"]
CMD ["dc", "-exec"]
