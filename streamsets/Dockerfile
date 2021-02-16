FROM streamsets/datacollector:3.21.0
ENV SDC_LIBS=streamsets-datacollector-all
USER root
RUN if [ -n "${SDC_LIBS}" ]; then "${SDC_DIST}/bin/streamsets" stagelibs -install="${SDC_LIBS}"; fi
USER ${SDC_USER}
EXPOSE 18630
COPY ./docker-entrypoint.sh /
COPY ./form-realm.properties ${SDC_CONF}/
ENTRYPOINT ["./docker-entrypoint.sh"]
CMD ["dc", "-exec"]