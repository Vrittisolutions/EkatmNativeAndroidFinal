uniform highp mat4 matProj;
uniform highp mat4 TexCoordsSizeMultMap;
uniform highp mat4 matWorldView;
uniform highp vec4 NeutralColor;
uniform highp mat4 matDataTransform;
uniform highp vec4 FixedSpriteSize;
attribute highp vec2 vPosition;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
attribute highp vec4 vTexCoord1;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_COLOR1;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec2 tmpvar_2;
  tmpvar_2.x = matDataTransform[0].x;
  tmpvar_2.y = matDataTransform[1].y;
  highp vec2 tmpvar_3;
  tmpvar_3.x = matDataTransform[0].w;
  tmpvar_3.y = matDataTransform[1].w;
  highp int tmpvar_4;
  tmpvar_4 = int(vTexCoord1.x);
  highp vec4 v_5;
  v_5.x = TexCoordsSizeMultMap[0][tmpvar_4];
  v_5.y = TexCoordsSizeMultMap[1][tmpvar_4];
  v_5.z = TexCoordsSizeMultMap[2][tmpvar_4];
  v_5.w = TexCoordsSizeMultMap[3][tmpvar_4];
  highp vec4 v_6;
  v_6.x = TexCoordsSizeMultMap[0][tmpvar_4];
  v_6.y = TexCoordsSizeMultMap[1][tmpvar_4];
  v_6.z = TexCoordsSizeMultMap[2][tmpvar_4];
  v_6.w = TexCoordsSizeMultMap[3][tmpvar_4];
  highp vec2 tmpvar_7;
  tmpvar_7 = ((v_6.xy - (vec2(0.5, 0.5) * FixedSpriteSize.zw)) * FixedSpriteSize.xy);
  highp vec4 tmpvar_8;
  tmpvar_8.zw = vec2(0.0, 1.0);
  tmpvar_8.xy = (((vPosition * tmpvar_2) + tmpvar_3) + ((tmpvar_7.x * vec2(1.0, 0.0)) + (tmpvar_7.y * vec2(0.0, 1.0))));
  highp vec2 tmpvar_9;
  tmpvar_9.x = v_5.x;
  tmpvar_9.y = (1.0 - v_5.y);
  highp vec4 tmpvar_10;
  tmpvar_10.zw = vec2(0.0, 1.0);
  tmpvar_10.xy = tmpvar_8.xy;
  tmpvar_1 = (tmpvar_10 * (matWorldView * matProj));
  tmpvar_1.y = -(tmpvar_1.y);
  highp vec4 tmpvar_11;
  tmpvar_11 = (vColor - NeutralColor);
  highp float tmpvar_12;
  tmpvar_12 = dot (tmpvar_11, tmpvar_11);
  float tmpvar_13;
  if ((tmpvar_12 > 0.0)) {
    tmpvar_13 = 1.0;
  } else {
    tmpvar_13 = 0.0;
  };
  highp vec4 tmpvar_14;
  tmpvar_14 = mix (vec4(1.0, 1.0, 1.0, 1.0), vColor, vec4(tmpvar_13));
  highp vec4 tmpvar_15;
  tmpvar_15 = (vColor1 - NeutralColor);
  highp float tmpvar_16;
  tmpvar_16 = dot (tmpvar_15, tmpvar_15);
  float tmpvar_17;
  if ((tmpvar_16 > 0.0)) {
    tmpvar_17 = 1.0;
  } else {
    tmpvar_17 = 0.0;
  };
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_14;
  xlv_COLOR1 = mix (vec4(1.0, 1.0, 1.0, 1.0), vColor1, vec4(tmpvar_17));
  xlv_TEXCOORD0 = tmpvar_9;
}

