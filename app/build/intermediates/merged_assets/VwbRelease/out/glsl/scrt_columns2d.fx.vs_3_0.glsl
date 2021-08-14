uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
attribute highp vec2 vPosition;
attribute highp vec2 vTexCoord0;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
attribute highp vec2 vTexCoord1;
attribute highp vec2 vTexCoord2;
attribute highp vec4 vTexCoord3;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 tmpvar_2;
  highp vec2 tmpvar_3;
  highp vec4 tmpvar_4;
  tmpvar_2 = vec4(0.0, 0.0, 0.0, 0.0);
  tmpvar_3 = vec2(0.0, 0.0);
  tmpvar_4 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 tmpvar_5;
  if ((vTexCoord0.y == -1.0)) {
    highp vec4 tmpvar_6;
    tmpvar_6.zw = vec2(0.0, 1.0);
    tmpvar_6.xy = vPosition;
    tmpvar_5 = (tmpvar_6 * matWorldViewProj);
  } else {
    highp vec4 outPosition_7;
    highp vec4 tmpvar_8;
    tmpvar_8.zw = vec2(0.0, 1.0);
    tmpvar_8.xy = vPosition;
    highp vec4 tmpvar_9;
    tmpvar_9 = (tmpvar_8 * matWorldViewProj);
    outPosition_7 = (tmpvar_9 / abs(tmpvar_9.w));
    highp vec4 tmpvar_10;
    tmpvar_10.zw = vec2(0.0, 1.0);
    tmpvar_10.xy = vTexCoord2;
    highp vec4 tmpvar_11;
    tmpvar_11 = (tmpvar_10 * matWorldViewProj);
    highp vec2 tmpvar_12;
    tmpvar_12 = normalize(((tmpvar_11 / 
      abs(tmpvar_11.w)
    ).xy - outPosition_7.xy));
    outPosition_7.x = (outPosition_7.x - ((vTexCoord1.x * screenSize.z) * tmpvar_12.y));
    outPosition_7.y = (outPosition_7.y + ((vTexCoord1.x * screenSize.w) * tmpvar_12.x));
    tmpvar_5 = outPosition_7;
  };
  if ((vTexCoord1.x == -1.0)) {
    tmpvar_2 = vColor;
  } else {
    tmpvar_2 = vColor1;
  };
  tmpvar_3 = vTexCoord3.xy;
  tmpvar_4.xy = vTexCoord1;
  tmpvar_4.z = vTexCoord0.y;
  tmpvar_1.xzw = tmpvar_5.xzw;
  tmpvar_1.y = -(tmpvar_5.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_2;
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
}

