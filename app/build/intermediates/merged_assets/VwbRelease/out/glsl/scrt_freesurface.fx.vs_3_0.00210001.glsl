uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 DataPositionOffset;
uniform highp vec4 DisplacementAxesConstraints;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
attribute highp vec3 vNormal;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_TEXCOORD5;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1 = vPosition;
  highp mat4 vMatWorldViewProj_2;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  vMatWorldViewProj_2 = (matWorldView * matProj);
  highp vec3 tmpvar_6;
  highp vec3 offset_7;
  offset_7 = DataPositionOffset.xyz;
  if ((((
    (vTexCoord0.x <= -0.1)
   || 
    (vTexCoord0.x >= 1.1)
  ) || (vTexCoord0.y <= -0.1)) || (vTexCoord0.y >= 1.1))) {
    tmpvar_6 = DataPositionOffset.xyz;
  } else {
    highp vec3 tmpvar_8;
    if (bool(DisplacementAxesConstraints.w)) {
      tmpvar_8 = DisplacementAxesConstraints.xyz;
    } else {
      tmpvar_8 = (normalize(vPosition.xyz) * DisplacementAxesConstraints.xyz);
    };
    lowp vec4 tmpvar_9;
    tmpvar_9 = texture2DLod (HeightMapTexture, vTexCoord0.xy, 0.0);
    highp float tmpvar_10;
    tmpvar_10 = tmpvar_9.x;
    offset_7 = (DataPositionOffset.xyz + (tmpvar_8 * vec3(tmpvar_10)));
    tmpvar_6 = offset_7;
  };
  tmpvar_1.xyz = (vPosition.xyz + tmpvar_6);
  highp mat3 tmpvar_11;
  tmpvar_11[0] = matWorld[0].xyz;
  tmpvar_11[1] = matWorld[1].xyz;
  tmpvar_11[2] = matWorld[2].xyz;
  tmpvar_5 = tmpvar_1;
  highp float tmpvar_12;
  tmpvar_12 = sqrt(dot (tmpvar_1.xyz, tmpvar_1.xyz));
  tmpvar_1.w = 1.0;
  tmpvar_3 = (tmpvar_1 * vMatWorldViewProj_2);
  tmpvar_4.zw = vec2(0.0, 0.0);
  tmpvar_4.xy = vTexCoord0.xy;
  gl_Position = tmpvar_3;
  xlv_TEXCOORD0 = tmpvar_4;
  xlv_TEXCOORD1 = tmpvar_5;
  xlv_TEXCOORD2 = (vNormal * tmpvar_11);
  xlv_TEXCOORD5 = tmpvar_3;
  xlv_TEXCOORD6 = tmpvar_12;
  xlv_TEXCOORD7 = vTexCoord0;
}

