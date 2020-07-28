module Main exposing(main, photoDecoder)

import Browser
import Http
import Html exposing (..)
import Html.Attributes exposing (class)
import Json.Decode exposing (Decoder, list, int, string, succeed)
import Json.Decode.Pipeline exposing (required)
import Env as Env

type alias Feed =
  { votes: List Vote }
  
type Msg 
  = LoadFeed (Result Http.Error Feed)

type alias Vote =
  { vote : Int
  , userId : String
  , location : String
  , date : String
  }

type alias Model = 
  { feed : Maybe Feed 
  , error : Maybe Http.Error 
  }

main : Program () Model Msg
main = 
  Browser.element 
    { init = init 
    , view = view
    , update = update
    , subscriptions = subscriptions 
    }

init : () -> (Model, Cmd Msg)
init _ = 
  (initialModel, fetchFeed)

view : Model -> Html Msg
view model  = 
  div [] 
    [ div [class "header"] 
      [ h1 [] [ text "Happiness"]]
    , div [class "content-flow"]
      [ viewContent model ]
    ]

viewContent : Model -> Html Msg
viewContent model = 
  case model.error of
     Just error -> 
      div [class "feed error"]
        [ text (errorMessage error)]
     Nothing ->
      viewFeed model.feed

initialModel : Model
initialModel = 
  { feed = Nothing 
  , error = Nothing
  }

update : Msg -> Model -> (Model, Cmd Msg) 
update msg model = 
  case msg of
    LoadFeed (Ok feed) ->
      ( {model| feed = Just feed } 
      , Cmd.none
      )
    
    LoadFeed (Err error) ->
      ( {model| error = Just error }
      , Cmd.none 
      )

subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
  
viewFeed : Maybe Feed -> Html Msg
viewFeed maybeFeed =
    case maybeFeed of
      Just feed -> 
        div [] (List.map viewDetailedPhoto feed.votes) 

      Nothing ->
        div [ class "loading-feed"]
          [ text "Loading Feed..."]

viewDetailedPhoto : Vote -> Html Msg
viewDetailedPhoto vote = 
  div [ class "detailed-vote"]
    [ div [class "vote-info"]
      [ h2 [class "caption"]
        [ text ("UserId:" ++ vote.userId) ]
      , text vote.location
      , br [] []
      , text vote.date
      , br [] []
      , text (String.fromInt vote.vote)
      ]
    ]

errorMessage : Http.Error -> String
errorMessage error = 
  case error of
    Http.BadBody _ -> 
      """Sorry, we couldn't process your feed at this time.
      We're working on it!"""
    _ -> 
      """Sorry, we couldn't load your feed at this time.
      Please try again later."""

fetchFeed : Cmd Msg
fetchFeed =
  Http.get 
    { url = Env.baseUrl ++ "/happiness/votes"
    , expect = Http.expectJson LoadFeed votesDecoder
    }

photoDecoder : Decoder Vote 
photoDecoder = 
  succeed Vote
    |>required "vote" int
    |>required "userId" string
    |>required "location" string
    |>required "date" string

votesDecoder : Decoder Feed 
votesDecoder = succeed Feed
  |>required "votes" (list photoDecoder)
