precision mediump float;
uniform sampler2D DiffuseTexture;
uniform sampler2D StrokeSpriteTexture;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_COLOR1;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  lowp vec4 tmpvar_1;
  tmpvar_1 = texture2D (DiffuseTexture, xlv_TEXCOORD0);
  lowp vec4 tmpvar_2;
  tmpvar_2 = texture2D (StrokeSpriteTexture, xlv_TEXCOORD0);
  highp vec4 tmpvar_3;
  tmpvar_3 = (xlv_COLOR1 * tmpvar_2);
  highp vec4 tmpvar_4;
  tmpvar_4 = mix ((xlv_COLOR0 * tmpvar_1), tmpvar_3, tmpvar_3.wwww);
  gl_FragData[0] = tmpvar_4;
}

