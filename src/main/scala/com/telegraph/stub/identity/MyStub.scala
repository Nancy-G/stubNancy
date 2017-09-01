package com.telegraph.stub.identity

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.Scenario

import scala.io.Source

/**
  * Created by toorap on 17/08/2017.
  * Example of a stub encapsulating contract and state
  */
object MyStub extends BaseStub {


  // map of action -> map  of pre-state, post-state
  override var stateTransitions =    Map(
    "POST" -> Map("registered" -> "registered"))


  override def setUpMocks(cannedResponsesPath: String): Unit  = {

    // happy path get
    wireMockServer.stubFor(post(urlMatching(".*/tokens"))
        .withRequestBody(equalToJson("{\"grant_type\":\"password\"}",true,true))
        .withRequestBody(equalToJson("{\"credential_type\":\"EMAIL_PASSWORD\"}",true,true))
        .willReturn(
          aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(Source.fromFile(cannedResponsesPath+"/tokensPasswordGrantHappy.json").mkString)
            .withStatus(200)))
  }

  // driver class
  def main(args : Array[String]) {

    // passed swagger file, port, canned responses file path, opening state
    MyStub.configureStub(args(0), args(1).toInt, args(2), "registered")
    MyStub.start
  }
}